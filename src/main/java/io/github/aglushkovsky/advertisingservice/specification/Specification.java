package io.github.aglushkovsky.advertisingservice.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.function.Function;

@FunctionalInterface
public interface Specification<T> {

    Predicate convertToPredicate(CriteriaQuery<T> query, CriteriaBuilder cb, Root<T> root);

    default <P> Specification<T> and(P object, Function<P, Specification<T>> function) {
        if (object == null) {
            return this;
        }

        return SpecificationCombiner.combine(this, function.apply(object), CriteriaBuilder::and);
    }

    default <P> Specification<T> or(P object, Function<P, Specification<T>> function) {
        if (object == null) {
            return this;
        }

        return SpecificationCombiner.combine(this, function.apply(object), CriteriaBuilder::or);
    }

    static <T> Specification<T> empty() {
        return (query, cb, root) -> null;
    }
}
