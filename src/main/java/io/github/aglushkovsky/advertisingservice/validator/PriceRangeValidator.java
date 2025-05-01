package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.dto.request.SearchAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidPriceRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, SearchAdsFilterRequestDto> {

    @Override
    public boolean isValid(SearchAdsFilterRequestDto value, ConstraintValidatorContext context) {
        BigDecimal minPrice = value.minPrice();
        BigDecimal maxPrice = value.maxPrice();

        return minPrice == null || maxPrice == null || maxPrice.compareTo(minPrice) >= 0;
    }
}
