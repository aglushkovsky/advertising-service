package io.github.aglushkovsky.advertisingservice.dto.response;

import io.github.aglushkovsky.advertisingservice.dto.LocalityDto;
import io.github.aglushkovsky.advertisingservice.dto.PublisherDto;

import java.math.BigDecimal;
import java.util.List;

public record AdResponseDto(Long id,
                            String title,
                            BigDecimal price,
                            String description,
                            List<LocalityDto> localityParts,
                            PublisherDto publisher,
                            String publishedAt,
                            Boolean isPromoted) {
}
