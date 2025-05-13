package io.github.aglushkovsky.advertisingservice.dao.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
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

    public Optional<UserRate> findByRecipientId(Long recipientId) {
        return findByPredicate(userRate.recipient.id.eq(recipientId));
    }

    public Optional<UserRate> findByAuthorId(Long authorId, Long recipientId) {
        return findByPredicate(
                userRate.author.id.eq(authorId)
                        .and(userRate.recipient.id.eq(recipientId))
        );
    }

    private Optional<UserRate> findByPredicate(Predicate predicate) {
        UserRate result = new JPAQuery<>(entityManager)
                .select(userRate)
                .from(userRate)
                .where(predicate)
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
