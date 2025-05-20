package io.github.aglushkovsky.advertisingservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserRateResponseDto(Long id, Double value, LocalDateTime createdAt) {
}
