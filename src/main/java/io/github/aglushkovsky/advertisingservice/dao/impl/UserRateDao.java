package io.github.aglushkovsky.advertisingservice.dao.impl;

import io.github.aglushkovsky.advertisingservice.dao.AbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QUserRate.*;

@Repository
@Transactional
public class UserRateDao extends AbstractDao<UserRate, Long> {

    @Override
    @Transactional(readOnly = true)
    public List<UserRate> findAll() {
        return findAll(userRate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRate> findById(Long id) {
        return findById(UserRate.class, id);
    }
}
