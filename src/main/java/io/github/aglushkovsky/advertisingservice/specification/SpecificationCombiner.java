package io.github.aglushkovsky.advertisingservice.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

public class SpecificationCombiner {

    public interface Combiner {
        Predicate combine(CriteriaBuilder builder, Predicate first, Predicate second);
    }

    public static <T> Specification<T> combine(Specification<T> firstSpec,
                                               Specification<T> secondSpec,
                                               Combiner combiner) {
        return (query, cb, root) -> {
            Predicate firstPredicate = firstSpec.convertToPredicate(query, cb, root);
            Predicate secondPredicate = secondSpec.convertToPredicate(query, cb, root);

            if (firstPredicate == null) {
                return secondPredicate;
            }
            if (secondPredicate == null) {
                return firstPredicate;
            }

            return combiner.combine(cb, firstPredicate, secondPredicate);
        };
    }
}
