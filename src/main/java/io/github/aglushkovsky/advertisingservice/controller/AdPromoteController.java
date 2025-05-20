package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.AdPromoteControllerDocs;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdPromoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ads/{id}/promote")
@RequiredArgsConstructor
@Slf4j
public class AdPromoteController implements AdPromoteControllerDocs {

    private final AdPromoteService adPromoteService;

    @PatchMapping
    public AdResponseDto promoteAd(@PathVariable Long id) {
        log.info("Start PATCH /api/v1/ads/{}/promote", id);
        AdResponseDto adResponseDto = adPromoteService.promoteAd(id);
        log.info("Finished PATCH /api/v1/ads/{}/promote", id);
        return adResponseDto;
    }
}
