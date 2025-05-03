package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdSearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdSearchController {

    private final AdSearchService adSearchService;

    @GetMapping
    public List<AdResponseDto> searchAds(@ModelAttribute("searchFilter") @Valid FindAllAdsFilterRequestDto filter) {
        log.info("Start GET /ads; params={}", filter);
        List<AdResponseDto> result = adSearchService.findAll(filter);
        log.info("Finished GET /ads; found items on page {}: {}", filter.page(), result.size());
        return result;
    }

    @GetMapping("/{id}")
    public AdResponseDto findById(@PathVariable @Positive Long id) {
        log.info("Start GET /ads/{}; id={}", id, id);
        AdResponseDto result = adSearchService.findById(id);
        log.info("Finished GET /ads/{}; id={}", id, id);
        return result;
    }

    @ModelAttribute("searchFilter")
    public FindAllAdsFilterRequestDto createFilterAttributes(FindAllAdsFilterRequestDto filterRequestDto,
                                                             @RequestParam(defaultValue = "false") Boolean onlyInTerm,
                                                             @RequestParam(defaultValue = "50") Long limit,
                                                             @RequestParam(defaultValue = "1") Long page) {
        return new FindAllAdsFilterRequestDto(
                filterRequestDto.term(),
                onlyInTerm,
                filterRequestDto.minPrice(),
                filterRequestDto.maxPrice(),
                filterRequestDto.publisherId(),
                filterRequestDto.localityId(),
                limit,
                page
        );
    }
}
