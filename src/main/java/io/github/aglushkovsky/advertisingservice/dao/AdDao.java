package io.github.aglushkovsky.advertisingservice.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import io.github.aglushkovsky.advertisingservice.entity.*;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QAd.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocality.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocalityPart.*;
import static io.github.aglushkovsky.advertisingservice.util.QueryDslUtils.*;

@Repository
public class AdDao extends AbstractDao<Ad, Long> {

    @Override
    public void delete(Long id) {
        delete(Ad.class, id);
    }

    @Override
    public List<Ad> findAll() {
        return findAll(ad);
    }

    public List<Ad> findAll(Long limit, Long page, Predicate predicate, OrderSpecifier<?>... orders) {
        return createFindAllQuery(ad)
                .where(predicate)
                .orderBy(orders)
                .limit(limit)
                .offset(calculateOffset(limit, page))
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), getEntityGraph())
                .fetch();
    }

    private Long calculateOffset(Long limit, Long page) {
        return (page - 1) * limit;
    }

    private EntityGraph<Ad> getEntityGraph() {
        EntityGraph<Ad> entityGraph = entityManager.createEntityGraph(Ad.class);
        entityGraph.addAttributeNodes(getName(ad.locality), getName(ad.publisher));

        Subgraph<Locality> localitySubgraph = entityGraph.addSubgraph(getName(ad.locality));
        localitySubgraph.addAttributeNodes(getName(locality.ancestors));

        Subgraph<Object> localityInLocalityParentsSubgraph = localitySubgraph.addSubgraph(getName(locality.ancestors));
        localityInLocalityParentsSubgraph.addAttributeNodes(getName(localityPart.ancestorLocality));

        return entityGraph;
    }

    public Long getTotalPageCount(Predicate predicate) {
        return new JPAQuery<>(entityManager)
                .select(ad.id.count())
                .from(ad)
                .where(predicate)
                .fetchOne();
    }

    @Override
    public Optional<Ad> findById(Long id) {
        Ad result = createFindAllQuery(ad)
                .where(ad.id.eq(id))
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), getEntityGraph())
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
