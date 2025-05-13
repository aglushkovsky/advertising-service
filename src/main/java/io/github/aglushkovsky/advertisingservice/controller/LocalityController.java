package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.service.LocalityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/localities")
@RequiredArgsConstructor
@SecurityRequirements
@Slf4j
public class LocalityController {

    private final LocalityService localityService;

    @GetMapping
    public List<LocalityResponseDto> findAllByLocalityType(@RequestParam(defaultValue = "CITY") LocalityType type) {
        log.info("Start GET /api/v1/localities; type={}", type);
        List<LocalityResponseDto> result = localityService.findAllByLocalityType(type);
        log.info("Finished GET /api/v1/localities; type={}; found items: {}", type, result.size());
        return result;
    }

    @GetMapping("/{id}/descendants")
    public List<LocalityResponseDto> findDirectDescendantsByLocalityId(@PathVariable @Min(1) Long id) {
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
