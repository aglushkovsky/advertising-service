package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AdCreateResponseDto(@NotBlank @Max(128) String title,
                                  @NotNull BigDecimal price,
                                  String description,
                                  @Positive Long localityId) {
}
