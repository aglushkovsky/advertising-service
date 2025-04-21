package io.github.aglushkovsky.advertisingservice.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SpecificationUtils {

    public static <T> boolean isEmptySpecification(Specification<T> spec,
                                                   CriteriaQuery<T> query,
                                                   CriteriaBuilder cb,
                                                   Root<T> root) {
        return spec == null || spec.convertToPredicate(query, cb, root) == null;
    }
}
