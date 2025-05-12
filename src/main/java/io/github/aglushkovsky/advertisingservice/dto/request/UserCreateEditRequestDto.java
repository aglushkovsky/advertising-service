package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import jakarta.validation.constraints.*;

// FIXME NotBlank игнорируется при редактировании => можно поставить логин с blank-символами.
// TODO Попробовать заиспользовать MessageSource для сообщений, чтобы их так не хардкодить.
public record UserCreateEditRequestDto(
        @NotBlank(groups = CreateGroup.class, message = "Логин не может быть пустым")
        @Size(min = 5, max = 50, message = "Длина должна быть от {min} до {max} символов")
        String login,
        @NotBlank(groups = CreateGroup.class, message = "Пароль не может быть пустым")
        String password,
        @Email(message = "Некорректный формат email")
        @Size(min = 5, max = 50, message = "Длина должна быть от {min} до {max} символов")
        String email,
        @Pattern(regexp = "^(?:\\+7|8)\\d{10}$", message = "Телефон должен быть в формате +7XXXXXXXXXX")
        String phoneNumber) {
}
