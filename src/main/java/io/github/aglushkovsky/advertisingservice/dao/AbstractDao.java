package io.github.aglushkovsky.advertisingservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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

    protected List<E> findAll(Class<E> aClass) {
        CriteriaQuery<E> query = createFindAllQuery(aClass, entityManager);
        return entityManager.createQuery(query).getResultList();
    }

    protected Optional<E> findById(Class<E> aClass, I id) {
        return Optional.ofNullable(entityManager.find(aClass, id));
    }

    private CriteriaQuery<E> createFindAllQuery(Class<E> aClass, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(aClass);
        Root<E> entity = query.from(aClass);
        query.select(entity);
        return query;
    }
}
