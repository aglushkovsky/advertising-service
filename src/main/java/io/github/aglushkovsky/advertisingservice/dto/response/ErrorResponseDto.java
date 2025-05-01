package io.github.aglushkovsky.advertisingservice.dto.response;

public record ErrorResponseDto<T>(int error, T body) {
}
