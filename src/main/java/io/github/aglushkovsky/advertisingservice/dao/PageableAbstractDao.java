package io.github.aglushkovsky.advertisingservice.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import org.hibernate.graph.GraphSemantic;

import java.util.List;
import java.util.Objects;

import static io.github.aglushkovsky.advertisingservice.entity.QAd.ad;
import static java.util.Collections.*;

public abstract class PageableAbstractDao<E, I> extends AbstractDao<E, I> {

    public PageEntity<E> findAll(Long limit,
                           Long page,
                           EntityPathBase<E> fromEntityPath,
                           EntityGraph<E> entityGraph,
                           Predicate predicate,
                           OrderSpecifier<?>... orders) {
        Long totalRecordsCount = getTotalRecordsCount(predicate);

        if (totalRecordsCount == 0) {
            return new PageEntity<>(
                    emptyList(), new PageEntity.Metadata(1L, 1L, totalRecordsCount, true)
            );
        }

        List<E> body = createFindAllQuery(fromEntityPath)
                .where(predicate)
                .orderBy(orders)
                .limit(limit)
                .offset(calculateOffset(limit, page))
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), entityGraph)
                .fetch();
        Long totalPages = calculateTotalPages(totalRecordsCount, limit);

        return new PageEntity<>(
                unmodifiableList(body),
                new PageEntity.Metadata(page, totalPages, totalRecordsCount, isLastPage(totalPages, page))
        );
    }

    private Long calculateOffset(Long limit, Long page) {
        return (page - 1) * limit;
    }

    private Long calculateTotalPages(Long totalRecordsCount, Long limit) {
        return (totalRecordsCount + limit - 1) / limit;
    }

    private boolean isLastPage(Long totalPages, Long currentPage) {
        return Objects.equals(totalPages, currentPage);
    }

    public Long getTotalRecordsCount(Predicate predicate) {
        return new JPAQuery<>(entityManager)
                .select(ad.id.count())
                .from(ad)
                .where(predicate)
                .fetchOne();
    }
}
