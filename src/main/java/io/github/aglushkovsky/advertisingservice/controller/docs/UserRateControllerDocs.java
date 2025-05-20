package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Tag(name = "User Rate", description = "Operations for working with user rates")
public interface UserRateControllerDocs {

    UserRateResponseDto createUserRate(@Min(1) Long recipientId, @Valid UserRateCreateRequestDto userRateCreateRequestDto);
}
