package io.github.aglushkovsky.advertisingservice.dto.response;

import java.util.List;

public record AdResponseDto(Long id,
                            String title,
                            Long price,
                            String description,
                            List<LocalityResponseDto> localityParts,
                            UserResponseDto publisher,
                            String publishedAt,
                            String status,
                            Boolean isPromoted) {
}
