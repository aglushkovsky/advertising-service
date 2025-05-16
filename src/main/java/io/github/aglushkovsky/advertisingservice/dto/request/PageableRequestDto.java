package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record PageableRequestDto(
        @Min(2)
        @Max(100)
        Long limit,

        @Min(1)
        Long page
) {
    public PageableRequestDto {
        if (limit == null) {
            limit = 50L;
        }
        if (page == null) {
            page = 1L;
        }
    }
}
