package io.github.aglushkovsky.advertisingservice.dto.response;

import lombok.Value;

@Value
public class JwtAuthResponseDto {
    String type = "Bearer";
    String accessToken;
}
