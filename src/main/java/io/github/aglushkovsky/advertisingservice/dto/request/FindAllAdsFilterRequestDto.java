package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidPriceRange;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
@ValidPriceRange(
        minPriceParam = "minPrice",
        maxPriceParam = "maxPrice"
)
public record FindAllAdsFilterRequestDto(
        String term,

        Boolean onlyInTitle,

        @PositiveOrZero(message = "Минимальная сумма не может быть меньше 0.")
        Long minPrice,

        @PositiveOrZero(message = "Максимальная сумма не может быть меньше 0.")
        Long maxPrice,

        @Min(
                value = 1,
                message = "Идентификатор автора не может быть меньше 1."
        )
        Long publisherId,

        @Min(
                value = 1,
                message = "Идентификатор адреса не может быть меньше 1."
        )
        Long localityId
) {
    public FindAllAdsFilterRequestDto {
        if (onlyInTitle == null) {
            onlyInTitle = false;
        }
    }
}
