package io.github.aglushkovsky.advertisingservice.dto.response;

import lombok.Builder;

@Builder
public record LocalityResponseDto(Long id,
                                  String name,
                                  String type) {
}
