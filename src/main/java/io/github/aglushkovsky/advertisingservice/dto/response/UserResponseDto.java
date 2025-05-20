package io.github.aglushkovsky.advertisingservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "User representation")
public record UserResponseDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "test_user")
        String login,

        @Schema(example = "example@example.com")
        String email,

        @Schema(example = "+79201234567")
        String phoneNumber,

        @Schema(example = "4.8")
        Double totalRating
) {
}
