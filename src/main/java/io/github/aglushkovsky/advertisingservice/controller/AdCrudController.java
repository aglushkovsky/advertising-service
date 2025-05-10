package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
@Slf4j
public class AdCrudController {

    private final AdCrudService adCrudService;

    @PostMapping
    public AdResponseDto createAd(@RequestBody @Valid AdCreateResponseDto adCreateResponseDto) {
        log.info("Start POST /api/v1/ads; {}", adCreateResponseDto);
        log.info("Finished POST /api/v1/ads; created ad: {}", (Object) null);
        return null;
    }
}