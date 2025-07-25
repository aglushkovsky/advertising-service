package io.github.aglushkovsky.advertisingservice.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractDao<E, I> implements Dao<E, I> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public E add(E e) {
        entityManager.persist(e);
        return e;
    }

    @Override
    public void delete(E e) {
        entityManager.remove(e);
    }

    @Override
    public void update(E e) {
        entityManager.merge(e);
    }

    @Override
    @Transactional(readOnly = true)
    public abstract List<E> findAll();

    @Override
    @Transactional(readOnly = true)
    public abstract Optional<E> findById(I id);

    protected List<E> findAll(EntityPathBase<E> aClass) {
        JPAQuery<E> findAllQuery = createFindAllQuery(aClass);
        return findAllQuery.fetch();
    }

    protected Optional<E> findById(Class<E> aClass, I id) {
        return Optional.ofNullable(entityManager.find(aClass, id));
    }

    protected JPAQuery<E> createFindAllQuery(EntityPathBase<E> fromEntityPath) {
        return new JPAQuery<>(entityManager)
                .select(fromEntityPath)
                .from(fromEntityPath);
    }

    protected boolean isExists(EntityPathBase<E> fromEntityPath, Predicate wherePredicate) {
        return new JPAQueryFactory(entityManager)
                .selectOne()
                .from(fromEntityPath)
                .where(wherePredicate)
                .fetchOne() != null;
    }
}
