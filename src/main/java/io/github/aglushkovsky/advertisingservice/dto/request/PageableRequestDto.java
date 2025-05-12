package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record PageableRequestDto(@Min(2) @Max(100) Long limit,
                                 @Min(1) Long page) {
}
