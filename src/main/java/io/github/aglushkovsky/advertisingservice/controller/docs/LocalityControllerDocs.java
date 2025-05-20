package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

import java.util.List;

@Tag(name = "Locality", description = "Operations for getting parts of localities")
@SecurityRequirements
public interface LocalityControllerDocs {

    List<LocalityResponseDto> findAllByLocalityType(LocalityType type);

    List<LocalityResponseDto> findDirectDescendantsByLocalityId(@Min(1) Long id);

    List<String> findAllAvailableLocalityTypes();
}
