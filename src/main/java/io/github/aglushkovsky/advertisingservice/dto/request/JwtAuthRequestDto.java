package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JwtAuthRequestDto(@NotBlank @Size(min = 5, max = 50) String login,
                                @NotBlank @Size(min = 5) String password) {
}
