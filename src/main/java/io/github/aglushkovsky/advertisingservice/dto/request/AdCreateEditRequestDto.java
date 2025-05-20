package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.AdStatus;
import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
@Schema(name = "Ad create or edit request")
public record AdCreateEditRequestDto(
        @NotBlank(
                groups = {CreateGroup.class},
                message = "Не указано название объявления"
        )
        @Size(
                min = 5, max = 128,
                message = "Название объявления должно быть от {min} до {max} символов"
        )
        @Schema(example = "Macbook Pro M4 16/512 новый запечатанный из ОАЭ с быстрой доставкой")
        String title,

        @NotNull(
                groups = CreateGroup.class,
                message = "Не указана цена"
        )
        @Min(
                value = 0,
                message = "Цена не должна быть меньше {value}"
        )
        @Schema(example = "17000000")
        Long price,

        @Size(
                min = 5,
                message = "Текст описания не может быть меньше {min} символов"
        )
        @Schema(example = "Зaпaкованныe и пoлнocтью нoвыe. Очень мощные и производительные, подxoдят под любые зaдачи.")
        String description,

        @NotNull(
                groups = CreateGroup.class,
                message = "Не указан адрес объявления"
        )
        @Min(
                value = 1,
                message = "Идентификатор адреса не должен быть меньше {value}"
        )
        Long localityId,

        Boolean isPromoted,

        AdStatus status) {
}
