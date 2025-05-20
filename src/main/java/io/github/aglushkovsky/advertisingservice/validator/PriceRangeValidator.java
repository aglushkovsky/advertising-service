package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidPriceRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, FindAllAdsFilterRequestDto> {

    @Override
    public boolean isValid(FindAllAdsFilterRequestDto value, ConstraintValidatorContext context) {
        Long minPrice = value.minPrice();
        Long maxPrice = value.maxPrice();

        if (minPrice == null || maxPrice == null) {
            log.info("Min price or max price is null, skip validation");
            return true;
        }

        log.info("Checking price range for minPrice={} and maxPrice={}", minPrice, maxPrice);

        boolean result = maxPrice.compareTo(minPrice) >= 0;

        if (!result) {
            log.error("minPrice={} and maxPrice={} is invalid price range", minPrice, maxPrice);
        } else {
            log.info("Finished isValid successfully; id={}", result);
        }

        return result;
    }
}
