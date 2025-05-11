package io.github.aglushkovsky.advertisingservice.dto.response;

import java.time.LocalDateTime;

public record CommentResponseDto(Long id,
                                 UserResponseDto author,
                                 LocalDateTime createdAt,
                                 String text) {
}
