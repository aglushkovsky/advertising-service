package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdCrudService;
import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
@Slf4j
// TODO Добавить логирование!
public class AdCrudController {

    private final AdCrudService adCrudService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdResponseDto createAd(@RequestBody @Validated(CreateGroup.class)
                                  AdCreateEditResponseDto adCreateEditResponseDto) {
        log.info("Start POST /api/v1/ads; {}", adCreateEditResponseDto);
        AdResponseDto response = adCrudService.createAd(adCreateEditResponseDto);
        log.info("Finished POST /api/v1/ads; created ad: {}", response.id());
        return response;
    }

    @PatchMapping("/{id}")
    public AdResponseDto editAd(@PathVariable Long id,
                                @RequestBody @Valid AdCreateEditResponseDto adCreateEditResponseDto) {
        return adCrudService.editAd(id, adCreateEditResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        adCrudService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public AdResponseDto findById(@PathVariable @Positive Long id) {
        log.info("Start GET /api/v1/ads/{}; id={}", id, id);
        AdResponseDto result = adCrudService.findById(id);
        log.info("Finished GET /api/v1/ads/{}; id={}", id, id);
        return result;
    }
}