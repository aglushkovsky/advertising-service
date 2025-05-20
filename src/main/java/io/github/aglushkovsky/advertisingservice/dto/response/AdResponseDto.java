package io.github.aglushkovsky.advertisingservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(name = "Ad representation")
public record AdResponseDto(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Macbook Pro M4 16/512 новый запечатанный из ОАЭ")
        String title,

        @Schema(
                description = "This parameter is specified in minimum monetary units (for example, in kopeks).",
                example = "15000000"
        )
        Long price,

        @Schema(example = "Зaпaкованныe и пoлнocтью нoвыe. Очень мощныe и подxoдят под любые зaдачи.")
        String description,

        @Schema(description = "Returns in right hierarchy order")
        List<LocalityResponseDto> localityParts,

        UserResponseDto publisher,

        @Schema(example = "2025-05-19T19:11:26.165173")
        String publishedAt,

        @Schema(example = "ACTIVE")
        String status,

        @Schema(example = "true")
        Boolean isPromoted
) {
}
