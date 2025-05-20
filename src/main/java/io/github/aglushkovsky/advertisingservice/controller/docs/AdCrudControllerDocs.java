package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Ad crud", description = "CRUD operations with ads")
public interface AdCrudControllerDocs {

    AdResponseDto createAd(AdCreateEditRequestDto adCreateEditRequestDto);

    AdResponseDto editAd(Long id, AdCreateEditRequestDto adCreateEditRequestDto);

    ResponseEntity<?> deleteAd(Long id);

    @SecurityRequirements
    AdResponseDto findById(Long id);
}
