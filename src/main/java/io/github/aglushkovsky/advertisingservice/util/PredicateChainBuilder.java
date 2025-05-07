package io.github.aglushkovsky.advertisingservice.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PredicateChainBuilder {

    private final List<Predicate> predicates = new ArrayList<>();

    public static PredicateChainBuilder builder() {
        return new PredicateChainBuilder();
    }

    public <T> PredicateChainBuilder and(T object, Function<T, Predicate> predicateFunction) {
        if (object != null) {
            predicates.add(predicateFunction.apply(object));
        }
        return this;
    }

    public Predicate build() {
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}
