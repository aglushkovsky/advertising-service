package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao extends AbstractDao<User, Long> {
    @Override
    public void delete(Long id) {
        delete(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return findAll(User.class);
    }

    @Override
    public Optional<User> findById(Long id) {
        return findById(User.class, id);
    }
}
