package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.Locality;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocalityDao extends AbstractDao<Locality, Long> {

    @Override
    public void delete(Long id) {
        delete(Locality.class, id);
    }

    @Override
    public List<Locality> findAll() {
        return findAll(Locality.class);
    }

    @Override
    public Optional<Locality> findById(Long id) {
        return findById(Locality.class, id);
    }

    public List<Locality> findAllParentLocalities(Locality leafLocality) {
        return entityManager.createNamedQuery("getFullLocalityHierarchyQuery", Locality.class)
                .setParameter("leafLocalityId", leafLocality.getId())
                .getResultList();
    }
}
