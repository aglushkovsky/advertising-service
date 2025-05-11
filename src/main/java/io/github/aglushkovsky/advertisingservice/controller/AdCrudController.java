package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdCrudService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
@Slf4j
public class AdCrudController {

    private final AdCrudService adCrudService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdResponseDto createAd(@RequestBody @Valid AdCreateResponseDto adCreateResponseDto) {
        log.info("Start POST /api/v1/ads; {}", adCreateResponseDto);
        log.info("Finished POST /api/v1/ads; created ad: {}", (Object) null);
        return null;
    }

    @GetMapping("/{id}")
    public AdResponseDto findById(@PathVariable @Positive Long id) {
        log.info("Start GET /api/v1/ads/{}; id={}", id, id);
        AdResponseDto result = adCrudService.findById(id);
        log.info("Finished GET /api/v1/ads/{}; id={}", id, id);
        return result;
    }
}