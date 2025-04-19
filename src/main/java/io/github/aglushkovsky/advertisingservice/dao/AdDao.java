package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.Ad;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdDao extends AbstractDao<Ad, Long> {
    @Override
    public void delete(Long id) {
        delete(Ad.class, id);
    }

    @Override
    public List<Ad> findAll() {
        return findAll(Ad.class);
    }

    @Override
    public Optional<Ad> findById(Long id) {
        return findById(Ad.class, id);
    }
}
