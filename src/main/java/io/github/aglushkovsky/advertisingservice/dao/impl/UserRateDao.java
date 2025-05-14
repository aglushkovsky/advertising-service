package io.github.aglushkovsky.advertisingservice.dao.impl;

import io.github.aglushkovsky.advertisingservice.dao.AbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QUserRate.*;

@Repository
public class UserRateDao extends AbstractDao<UserRate, Long> {

    @Override
    public List<UserRate> findAll() {
        return findAll(userRate);
    }

    @Override
    public Optional<UserRate> findById(Long id) {
        return findById(UserRate.class, id);
    }

    public List<UserRate> findByRecipientId(Long recipientId) {
        return createFindAllQuery(userRate)
                .where(userRate.recipient.id.eq(recipientId))
                .fetch();
    }

    public Optional<UserRate> findByAuthorAndRecipientId(Long authorId, Long recipientId) {
        UserRate result = createFindAllQuery(userRate)
                .where(userRate.author.id.eq(authorId)
                        .and(userRate.recipient.id.eq(recipientId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
