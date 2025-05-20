package io.github.aglushkovsky.advertisingservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Locality representation")
public record LocalityResponseDto(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Орёл")
        String name,

        @Schema(example = "CITY")
        String type
) {
}
