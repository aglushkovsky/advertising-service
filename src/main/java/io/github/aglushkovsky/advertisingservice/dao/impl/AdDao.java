package io.github.aglushkovsky.advertisingservice.dao.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.PageableAbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.*;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QAd.*;
import static io.github.aglushkovsky.advertisingservice.entity.QComment.comment;
import static io.github.aglushkovsky.advertisingservice.entity.QLocality.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocalityPart.*;
import static io.github.aglushkovsky.advertisingservice.util.QueryDslUtils.*;

@Repository
@Transactional
public class AdDao extends PageableAbstractDao<Ad, Long> {

    @Override
    public void delete(Long id) {
        delete(Ad.class, id);
    }

    // TODO Подумать, какой вариант delete оставить
    public void delete(Ad ad) {
        entityManager.remove(ad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ad> findAll() {
        return findAll(ad);
    }

    @Transactional(readOnly = true)
    public PageEntity<Ad> findAll(Long limit, Long page, Predicate predicate, OrderSpecifier<?>... orders) {
        return findAll(limit, page, ad, getEntityGraph(), predicate, orders);
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

    @Override
    @Transactional(readOnly = true)
    public Optional<Ad> findById(Long id) {
        Ad result = createFindAllQuery(ad)
                .where(ad.id.eq(id))
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), getEntityGraph())
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Transactional(readOnly = true)
    public boolean isExists(Long adId) {
        return isExists(ad, ad.id.eq(adId));
    }
}
