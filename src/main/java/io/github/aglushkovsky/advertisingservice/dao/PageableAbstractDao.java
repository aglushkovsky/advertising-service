package io.github.aglushkovsky.advertisingservice.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import org.hibernate.graph.GraphSemantic;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.*;

public abstract class PageableAbstractDao<E, I> extends AbstractDao<E, I> {

    public PageEntity<E> findAll(Long limit,
                                 Long page,
                                 EntityPathBase<E> fromEntityPath,
                                 Predicate predicate,
                                 OrderSpecifier<?>... orders) {
        return findAll(limit, page, fromEntityPath, null, predicate, orders);
    }

    public PageEntity<E> findAll(Long limit,
                           Long page,
                           EntityPathBase<E> fromEntityPath,
                           EntityGraph<E> entityGraph,
                           Predicate predicate,
                           OrderSpecifier<?>... orders) {
        Long totalRecordsCount = getTotalRecordsCount(fromEntityPath, predicate);

        if (totalRecordsCount == 0) {
            return new PageEntity<>(
                    emptyList(),
                    new PageEntity.Metadata(1L, 1L, totalRecordsCount, true)
            );
        }

        JPAQuery<E> findAllJpaQuery = buildFindAllJpaQuery(limit, page, fromEntityPath, entityGraph, predicate, orders);
        Long totalPages = calculateTotalPages(totalRecordsCount, limit);
        List<E> body = findAllJpaQuery.fetch();

        return new PageEntity<>(
                unmodifiableList(body),
                new PageEntity.Metadata(page, totalPages, totalRecordsCount, isLastPage(totalPages, page))
        );
    }

    private JPAQuery<E> buildFindAllJpaQuery(Long limit,
                                             Long page,
                                             EntityPathBase<E> fromEntityPath,
                                             EntityGraph<E> entityGraph,
                                             Predicate predicate,
                                             OrderSpecifier<?>... orders) {
        JPAQuery<E> findAllJpaQuery = createFindAllQuery(fromEntityPath);

        if (predicate != null) {
            findAllJpaQuery.where(predicate);
        }
        if (orders != null) {
            findAllJpaQuery.orderBy(orders);
        }
        if (limit != null) {
            findAllJpaQuery.limit(limit);
        }
        if (page != null) {
            findAllJpaQuery.offset(calculateOffset(limit, page));
        }
        if (entityGraph != null) {
            findAllJpaQuery.setHint(GraphSemantic.LOAD.getJakartaHintName(), entityGraph);
        }

        return findAllJpaQuery;
    }

    private Long getTotalRecordsCount(EntityPathBase<E> fromEntityPath, Predicate predicate) {
        return new JPAQuery<>(entityManager)
                .select(fromEntityPath.count())
                .from(fromEntityPath)
                .where(predicate)
                .fetchOne();
    }

    // TODO Похоже на нарушение SRP. Подумать, куда эти методы вынести.

    private Long calculateOffset(Long limit, Long page) {
        return (page - 1) * limit;
    }

    private Long calculateTotalPages(Long totalRecordsCount, Long limit) {
        return (totalRecordsCount + limit - 1) / limit;
    }

    private boolean isLastPage(Long totalPages, Long currentPage) {
        return Objects.equals(totalPages, currentPage);
    }
}
