package io.github.aglushkovsky.advertisingservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDto(Long id,
                                 UserResponseDto author,
                                 LocalDateTime createdAt,
                                 String text) {
}
