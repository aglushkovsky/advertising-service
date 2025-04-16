package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRateDao extends AbstractDao<UserRate, Long> {
    @Override
    public void delete(Long id) {
        delete(UserRate.class, id);
    }

    @Override
    public List<UserRate> findAll() {
        return findAll(UserRate.class);
    }

    @Override
    public Optional<UserRate> findById(Long id) {
        return findById(UserRate.class, id);
    }
}
