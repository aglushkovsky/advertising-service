package io.github.aglushkovsky.advertisingservice.dao.impl;

import com.querydsl.jpa.impl.JPAQuery;
import io.github.aglushkovsky.advertisingservice.dao.AbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QLocality.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocalityPart.*;

@Repository
public class LocalityDao extends AbstractDao<Locality, Long> {

    private static final Integer DIRECT_DESCENDANTS_DEPTH = 1;

    @Override
    public List<Locality> findAll() {
        return findAll(locality);
    }

    @Override
    public Optional<Locality> findById(Long id) {
        return findById(Locality.class, id);
    }

    public List<Locality> findDirectDescendantsByLocalityId(Long localityId) {
        return new JPAQuery<>(entityManager)
                .select(locality)
                .from(localityPart)
                .join(locality).on(locality.eq(localityPart.descendantLocality))
                .where(localityPart.ancestorLocality.id.eq(localityId)
                        .and(localityPart.depth.eq(DIRECT_DESCENDANTS_DEPTH)))
                .fetch();
    }

    public List<Locality> findAllByLocalityType(LocalityType localityType) {
        return new JPAQuery<>(entityManager)
                .select(locality)
                .from(locality)
                .where(locality.type.eq(localityType))
                .fetch();
    }

    @Transactional(readOnly = true)
    public boolean isExists(Long localityId) {
        return isExists(locality, locality.id.eq(localityId));
    }
}
