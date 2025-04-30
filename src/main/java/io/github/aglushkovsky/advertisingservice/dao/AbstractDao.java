package io.github.aglushkovsky.advertisingservice.dao;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<E, I> implements Dao<E, I> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public E add(E e) {
        entityManager.persist(e);
        return e;
    }

    protected void delete(Class<E> aClass, I id) {
        E e = entityManager.find(aClass, id);
        entityManager.remove(e);
    }

    @Override
    public void update(E e) {
        entityManager.merge(e);
    }

    protected List<E> findAll(EntityPathBase<E> aClass) {
        JPAQuery<E> findAllQuery = createFindAllQuery(aClass);
        return findAllQuery.fetch();
    }

    protected Optional<E> findById(Class<E> aClass, I id) {
        return Optional.ofNullable(entityManager.find(aClass, id));
    }

    protected JPAQuery<E> createFindAllQuery(EntityPathBase<E> entityPathBase) {
        return new JPAQuery<>(entityManager)
                .select(entityPathBase)
                .from(entityPathBase);
    }
}
