package io.github.aglushkovsky.advertisingservice.dto.response;

import java.time.LocalDateTime;

public record UserRateResponseDto(Long id, Double value, LocalDateTime createdAt) {
}
