package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageCreateRequestDto(
        @NotBlank(message = "Не указан текст сообщения")
        @Size(
                min = 5,
                message = "Текст сообщения не может быть меньше {min} символов"
        )
        String text) {
}
