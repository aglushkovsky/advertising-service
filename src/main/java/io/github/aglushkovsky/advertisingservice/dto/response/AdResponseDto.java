package io.github.aglushkovsky.advertisingservice.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record AdResponseDto(Long id,
                            String title,
                            BigDecimal price,
                            String description,
                            List<LocalityResponseDto> localityParts,
                            PublisherResponseDto publisher,
                            String publishedAt,
                            Boolean isPromoted) {
}
