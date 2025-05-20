package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth")
public interface AuthControllerDocs {

    @SecurityRequirements
    JwtAuthResponseDto login(@Valid JwtAuthRequestDto request);
}
