package io.github.aglushkovsky.advertisingservice.dto.request;

import java.math.BigDecimal;

public record FindAllAdsFilterRequestDto(String term,
                                         Boolean onlyInTitle,
                                         BigDecimal minPrice,
                                         BigDecimal maxPrice,
                                         Long publisherId,
                                         Long localityId,
                                         Long limit,
                                         Long page) {
}
