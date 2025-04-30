package io.github.aglushkovsky.advertisingservice.dto.request;

public record FindAllAdsFilterRequestDto(String term,
                                         Boolean onlyInTitle,
                                         Long minPrice,
                                         Long maxPrice,
                                         Long publisherId,
                                         Long localityId,
                                         Long limit,
                                         Long page) {
}
