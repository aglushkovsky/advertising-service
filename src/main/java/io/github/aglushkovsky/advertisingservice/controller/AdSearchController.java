package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdSearchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads")
@SecurityRequirements
@RequiredArgsConstructor
@Slf4j
public class AdSearchController {

    private final AdSearchService adSearchService;

    @GetMapping
    public PageEntity<AdResponseDto> searchAds(@ModelAttribute("filter") @Valid FindAllAdsFilterRequestDto filter,
                                               @ModelAttribute("pageable") @Valid PageableRequestDto pageable) {
        log.info("Start GET /api/v1/ads; params={}", filter);
        PageEntity<AdResponseDto> result = adSearchService.findAll(filter, pageable);
        log.info("Finished GET /api/v1/ads; found items on page {}: {}", pageable.page(), result.body().size());
        return result;
    }

    @GetMapping("/{id}")
    public AdResponseDto findById(@PathVariable @Positive Long id) {
        log.info("Start GET /api/v1/ads/{}; id={}", id, id);
        AdResponseDto result = adSearchService.findById(id);
        log.info("Finished GET /api/v1/ads/{}; id={}", id, id);
        return result;
    }

    @ModelAttribute("filter")
    public FindAllAdsFilterRequestDto createFilterAttributes(FindAllAdsFilterRequestDto filterRequestDto,
                                                             @RequestParam(defaultValue = "false") Boolean onlyInTerm) {
        return new FindAllAdsFilterRequestDto(
                filterRequestDto.term(),
                onlyInTerm,
                filterRequestDto.minPrice(),
                filterRequestDto.maxPrice(),
                filterRequestDto.publisherId(),
                filterRequestDto.localityId()
        );
    }

    @ModelAttribute("pageable")
    public PageableRequestDto createPageableAttributes(@RequestParam(defaultValue = "50") Long limit,
                                                       @RequestParam(defaultValue = "1") Long page) {
        return new PageableRequestDto(limit, page);
    }
}
