package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.controller.ScrollDirection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ScrollableRequestDto(
        @NotNull
        @Min(1)
        Long startId,

        @Min(1)
        @Max(50)
        Long limit,

        ScrollDirection scrollDirection
) {
    public ScrollableRequestDto {
        if (limit == null) {
            limit = 10L;
        }
        if (scrollDirection == null) {
            scrollDirection = ScrollDirection.DOWN;
        }
    }
}
