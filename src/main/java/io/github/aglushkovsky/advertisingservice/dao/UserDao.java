package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QUser.*;

@Repository
public class UserDao extends AbstractDao<User, Long> {
    @Override
    public void delete(Long id) {
        delete(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return findAll(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return findById(User.class, id);
    }

    public boolean isExists(Long id) {
        return isExists(user, user.id.eq(id));
    }
}
