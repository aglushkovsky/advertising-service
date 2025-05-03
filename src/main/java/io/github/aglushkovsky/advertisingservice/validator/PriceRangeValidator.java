package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidPriceRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, FindAllAdsFilterRequestDto> {

    @Override
    public boolean isValid(FindAllAdsFilterRequestDto value, ConstraintValidatorContext context) {
        BigDecimal minPrice = value.minPrice();
        BigDecimal maxPrice = value.maxPrice();

        return minPrice == null || maxPrice == null || maxPrice.compareTo(minPrice) >= 0;
    }
}
