package io.github.aglushkovsky.advertisingservice.dao.impl;

import com.querydsl.jpa.impl.JPAQuery;
import io.github.aglushkovsky.advertisingservice.dao.AbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QUser.*;

@Repository
public class UserDao extends AbstractDao<User, Long> {

    @Override
    public List<User> findAll() {
        return findAll(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return findById(User.class, id);
    }

    @Transactional(readOnly = true)
    public boolean isExists(Long id) {
        return isExists(user, user.id.eq(id));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        User result = new JPAQuery<>(entityManager)
                .select(user)
                .from(user)
                .where(user.login.eq(login))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public void updateTotalRating(Long userId, Double totalRating) {
        String updateTotalRatingQuery = """
                UPDATE User u SET u.totalRating = :totalRating WHERE u.id = :userId
                """;

        entityManager.createQuery(updateTotalRatingQuery)
                .setParameter("totalRating", totalRating)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
