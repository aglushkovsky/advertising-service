package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.AdsSearchControllerDocs;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdsSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AdsSearchController implements AdsSearchControllerDocs {

    private final AdsSearchService adsSearchService;

    @GetMapping("/ads")
    public PageEntity<AdResponseDto> searchAds(FindAllAdsFilterRequestDto filter, PageableRequestDto pageable) {
        log.info("Start GET /api/v1/ads; params={}", filter);
        PageEntity<AdResponseDto> result = adsSearchService.findAll(filter, pageable);
        log.info("Finished GET /api/v1/ads; found items on page {}: {}", pageable.page(), result.body().size());
        return result;
    }

    @GetMapping("/users/{userId}/ads/history")
    public PageEntity<AdResponseDto> getAdsHistoryByUserId(@PathVariable Long userId, PageableRequestDto pageable) {
        log.info("Start GET /api/v1/users/{}/ads; params={}", userId, pageable);
        PageEntity<AdResponseDto> adsHistoryByUserId = adsSearchService.getAdsHistoryByUserId(userId, pageable);
        log.info("Finished GET /api/v1/users/{}/ads; found items on page {}: {}",
                userId, pageable.page(), adsHistoryByUserId.body().size());
        return adsHistoryByUserId;
    }
}
