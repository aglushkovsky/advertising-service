package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.service.LocalityService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/localities")
@RequiredArgsConstructor
@Slf4j
public class LocalityController {

    private final LocalityService localityService;

    @GetMapping
    public List<LocalityResponseDto> findAllByLocalityType(@RequestParam(defaultValue = "${root.locality.type}")
                                                           LocalityType type) {
        log.info("Start GET /api/v1/localities; type={}", type);
        List<LocalityResponseDto> result = localityService.findAllByLocalityType(type);
        log.info("Finished GET /api/v1/localities; type={}; found items: {}", type, result.size());
        return result;
    }

    @GetMapping("/{id}/descendants")
    public List<LocalityResponseDto> findDirectDescendantsByLocalityId(@PathVariable @Positive Long id) {
        log.info("Start GET /api/v1/localities/{}/descendants; localityId={}", id, id);
        List<LocalityResponseDto> result = localityService.findDirectDescendantsByLocalityId(id);
        log.info("Finished GET /api/v1/localities/{}/descendants; found items: {}", id, result.size());
        return result;
    }

    @GetMapping("/types")
    public List<String> findAllAvailableLocalityTypes() {
        log.info("Start GET /api/v1/localities/types");
        List<String> result = localityService.findAllAvailableLocalityTypes();
        log.info("Finished GET /api/v1/localities/types");
        return result;
    }
}
