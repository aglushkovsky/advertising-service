package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

// FIXME NotBlank игнорируется при редактировании => можно поставить логин с blank-символами
public record AdCreateEditResponseDto(
        @NotBlank(
                groups = {CreateGroup.class},
                message = "Не указано название объявления"
        )
        @Size(
                min = 5, max = 128,
                message = "Название объявления должно быть от {min} до {max} символов"
        )
        String title,

        @NotNull(
                groups = CreateGroup.class,
                message = "Не указана цена"
        )
        @Min(
                value = 0,
                message = "Цена не должна быть меньше {value}"
        )
        // FIXME Поскольку я решил, что будет оперировать копейками (целым числом), то BigDecimal здесь не уместен.
        BigDecimal price,

        @Size(
                min = 5,
                message = "Текст описания не может быть меньше {min} символов"
        )
        String description,

        @NotNull(
                groups = CreateGroup.class,
                message = "Не указан адрес объявления"
        )
        @Min(
                value = 1,
                message = "Идентификатор адреса не должен быть меньше {value}"
        )
        Long localityId) {
}
