package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.SearchAdsFilterRequestDto;
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

    // FIXME Подумать, как сделать передачу большого количества параметров фильтрации красивее.
    @GetMapping
    public List<AdResponseDto> searchAds(
            @Valid SearchAdsFilterRequestDto filter,
            @RequestParam(defaultValue = "false") Boolean onlyInTerm,
            @RequestParam(defaultValue = "50") @Positive Long limit,
            @RequestParam(defaultValue = "1") @Positive Long page
    ) {
        log.info("Start GET /ads; params={}, {}, {}, {}", filter, onlyInTerm, limit, page);
        FindAllAdsFilterRequestDto findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                filter.term(),
                onlyInTerm,
                filter.minPrice(),
                filter.maxPrice(),
                filter.publisherId(),
                filter.localityId(),
                limit,
                page);
        List<AdResponseDto> result = adSearchService.findAll(findAllAdsFilterRequestDto);
        log.info("Finished GET /ads; found items on page {}: {}", page, result.size());
        return result;
    }

    @GetMapping("/{id}")
    public AdResponseDto findById(@PathVariable @Positive Long id) {
        log.info("Start GET /ads/{}; id={}", id, id);
        AdResponseDto result = adSearchService.findById(id);
        log.info("Finished GET /ads/{}; id={}", id, id);
        return result;
    }
}
