package io.github.aglushkovsky.advertisingservice.dto.response;

import java.time.LocalDateTime;

public record MessageResponseDto(Long id,
                                 UserResponseDto author,
                                 LocalDateTime sentAt,
                                 String text) {
}
