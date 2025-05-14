package io.github.aglushkovsky.advertisingservice.dto.response;

import lombok.Builder;

@Builder
public record UserResponseDto(Long id,
                              String login,
                              String email,
                              String phoneNumber,
                              Double totalRating) {
}
