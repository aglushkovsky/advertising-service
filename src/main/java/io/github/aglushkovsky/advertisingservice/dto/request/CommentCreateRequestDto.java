package io.github.aglushkovsky.advertisingservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "Comment create request")
public record CommentCreateRequestDto(
        @NotBlank(message = "Не указан текст комментария")
        @Size(
                min = 5,
                message = "Текст комментария не может быть меньше {min} символов"
        )
        @Schema(
                description = "Comment text",
                example = "Съешь ещё этих мягких французских булок, да выпей чаю."
        )
        String text) {
}
