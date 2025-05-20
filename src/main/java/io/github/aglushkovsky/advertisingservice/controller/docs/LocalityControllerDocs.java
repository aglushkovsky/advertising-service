package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Locality", description = "Operations for getting parts of localities")
public interface LocalityControllerDocs {

    List<LocalityResponseDto> findAllByLocalityType(LocalityType type);

    List<LocalityResponseDto> findDirectDescendantsByLocalityId(Long id);

    List<String> findAllAvailableLocalityTypes();
}
