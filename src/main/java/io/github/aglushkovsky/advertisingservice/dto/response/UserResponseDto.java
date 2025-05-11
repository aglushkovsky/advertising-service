package io.github.aglushkovsky.advertisingservice.dto.response;

public record UserResponseDto(Long id,
                              String login,
                              String email,
                              String phoneNumber,
                              Double totalRating) {
}
