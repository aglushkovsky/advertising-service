package io.github.aglushkovsky.advertisingservice.dto.response;

public record ErrorObjectDto<T>(String parameter,
                                T value,
                                String message) {
}
