package io.github.aglushkovsky.advertisingservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageResponseDto(Long id,
                                 UserResponseDto author,
                                 LocalDateTime sentAt,
                                 String text) {
}
