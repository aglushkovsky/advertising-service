package io.github.aglushkovsky.advertisingservice.validator.annotation;

import io.github.aglushkovsky.advertisingservice.validator.PriceRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceRangeValidator.class)
public @interface ValidPriceRange {

    String message() default "{minPriceParam} и {maxPriceParam} задают некорректный диапазон";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String minPriceParam();

    String maxPriceParam();
}
