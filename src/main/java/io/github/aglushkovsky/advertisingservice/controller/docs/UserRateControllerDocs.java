package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Rate", description = "Operations for working with user rates")
public interface UserRateControllerDocs {

    UserRateResponseDto createUserRate(Long recipientId, UserRateCreateRequestDto userRateCreateRequestDto);
}
