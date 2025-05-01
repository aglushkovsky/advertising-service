package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidLocalityType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalityTypeValidator implements ConstraintValidator<ValidLocalityType, String> {

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        try {
            LocalityType.valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
