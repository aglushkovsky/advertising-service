package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.validator.annotation.ExistsIdInLocalityDao;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ExistsIdInUserDao;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidPriceRange;
import io.github.aglushkovsky.advertisingservice.validator.group.DaoValidationGroup;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@ValidPriceRange(
        minPriceParam = "minPrice",
        maxPriceParam = "maxPrice"
)
public record FindAllAdsFilterRequestDto(String term,
                                         Boolean onlyInTitle,
                                         @PositiveOrZero(message = "Минимальная сумма не может быть меньше 0.")
                                         BigDecimal minPrice,
                                         @PositiveOrZero(message = "Максимальная сумма не может быть меньше 0.")
                                         BigDecimal maxPrice,
                                         @ExistsIdInUserDao(groups = DaoValidationGroup.class)
                                         @Positive(message = "Идентификатор автора не может быть меньше 1.")
                                         Long publisherId,
                                         @Positive(message = "Идентификатор адреса не может быть меньше 1.")
                                         @ExistsIdInLocalityDao(groups = DaoValidationGroup.class)
                                         Long localityId) {
}
