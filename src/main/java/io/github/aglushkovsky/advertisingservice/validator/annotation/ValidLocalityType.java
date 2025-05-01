package io.github.aglushkovsky.advertisingservice.validator.annotation;

import io.github.aglushkovsky.advertisingservice.validator.LocalityTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalityTypeValidator.class)
public @interface ValidLocalityType {
    String message() default "Invalid locality type"; // TODO разобраться с default message
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
