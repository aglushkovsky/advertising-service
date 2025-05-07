package io.github.aglushkovsky.advertisingservice.validator.annotation;

import io.github.aglushkovsky.advertisingservice.validator.ExistsIdInUserDaoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistsIdInUserDaoValidator.class})
public @interface ExistsIdInUserDao {

    String message() default "Должно существовать в БД";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
