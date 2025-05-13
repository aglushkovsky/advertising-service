package io.github.aglushkovsky.advertisingservice.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E, I> {
    E add(E e);
    void delete(E e);
    void update(E e);
    List<E> findAll();
    Optional<E> findById(I id);
}
