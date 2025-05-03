package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidPriceRange;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@ValidPriceRange(minPriceParam = "minPrice", maxPriceParam = "maxPrice")
public record FindAllAdsFilterRequestDto(String term,
                                         Boolean onlyInTitle,
                                         @PositiveOrZero BigDecimal minPrice,
                                         @PositiveOrZero BigDecimal maxPrice,
                                         @Positive Long publisherId,
                                         @Positive Long localityId,
                                         @Min(2) Long limit,
                                         @Positive Long page) {
}
