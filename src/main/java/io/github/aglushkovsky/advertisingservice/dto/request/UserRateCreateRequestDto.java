package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UserRateCreateRequestDto(
        @NotNull
        @DecimalMin("1.0")
        @DecimalMax("5.0")
        Double value) {
}
