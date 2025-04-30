package io.github.aglushkovsky.advertisingservice.dto;

public record PublisherDto(Long id,
                           String login,
                           String email,
                           String phoneNumber,
                           Double totalRating) {
}
