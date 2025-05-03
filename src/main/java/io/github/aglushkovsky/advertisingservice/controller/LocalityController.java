package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.LocalityDto;
import io.github.aglushkovsky.advertisingservice.service.LocalityService;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ValidLocalityType;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/localities")
@RequiredArgsConstructor
@Slf4j
public class LocalityController {

    private final LocalityService localityService;

    @GetMapping
    public List<LocalityDto> findAllByLocalityType(@RequestParam(required = false, defaultValue = "${root.locality.type}")
                                                   @ValidLocalityType String type) {
        log.info("Start GET /localities; type={}", type);
        List<LocalityDto> result = localityService.findAllByLocalityType(type);
        log.info("Finished GET /localities; type={}; found items: {}", type, result.size());
        return result;
    }

    @GetMapping("/{localityId}/descendants")
    public List<LocalityDto> findDirectDescendantsByLocalityId(@PathVariable @Positive Long localityId) {
        log.info("Start GET /localities/{}/descendants; localityId={}", localityId, localityId);
        List<LocalityDto> result = localityService.findDirectDescendantsByLocalityId(localityId);
        log.info("Finished GET /localities/{}/descendants; found items: {}", localityId, result.size());
        return result;
    }

    @GetMapping("/types")
    public List<String> findAllAvailableLocalityTypes() {
        log.info("Start GET /localities/types");
        List<String> result = localityService.findAllAvailableLocalityTypes();
        log.info("Finished GET /localities/types");
        return result;
    }
}
