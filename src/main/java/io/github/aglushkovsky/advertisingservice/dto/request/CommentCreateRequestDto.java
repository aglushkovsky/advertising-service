package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CommentCreateRequestDto(
        @NotBlank(message = "Не указан текст комментария")
        @Size(
                min = 5,
                message = "Текст комментария не может быть меньше {min} символов"
        )
        String text) {
}
