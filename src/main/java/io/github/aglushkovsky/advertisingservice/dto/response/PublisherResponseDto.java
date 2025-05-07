package io.github.aglushkovsky.advertisingservice.dto.response;

public record PublisherResponseDto(Long id,
                                   String login,
                                   String email,
                                   String phoneNumber,
                                   Double totalRating) {
}
