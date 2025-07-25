package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.AdCrudControllerDocs;
import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
@Slf4j
public class AdCrudController implements AdCrudControllerDocs {

    private final AdCrudService adCrudService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdResponseDto createAd(@RequestBody AdCreateEditRequestDto adCreateEditRequestDto) {
        log.info("Start POST /api/v1/ads; {}", adCreateEditRequestDto);
        AdResponseDto response = adCrudService.createAd(adCreateEditRequestDto);
        log.info("Finished POST /api/v1/ads; created ad: {}", response.id());
        return response;
    }

    @PatchMapping("/{id}")
    public AdResponseDto editAd(@PathVariable Long id, @RequestBody AdCreateEditRequestDto adCreateEditRequestDto) {
        log.info("Start PATCH /api/v1/ads; id={}, {}", id, adCreateEditRequestDto);
        AdResponseDto adResponseDto = adCrudService.editAd(id, adCreateEditRequestDto);
        log.info("Finished PATCH /api/v1/ads; updated ad with id={}: {}", id, adResponseDto);
        return adResponseDto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        log.info("Start DELETE /api/v1/ads; id={}", id);
        adCrudService.deleteAd(id);
        log.info("Finished DELETE /api/v1/ads; deleted ad: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public AdResponseDto findById(@PathVariable Long id) {
        log.info("Start GET /api/v1/ads/{}; id={}", id, id);
        AdResponseDto result = adCrudService.findById(id);
        log.info("Finished GET /api/v1/ads/{}; id={}", id, id);
        return result;
    }
}