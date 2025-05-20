package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Ad crud", description = "CRUD operations with ads")
public interface AdCrudControllerDocs {

    @Operation(
            summary = "Create ad",
            description = "Endpoint for creating ad"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Returns created ad representation"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Returns when locality by specified id does not exists",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Resource Not Found",
                                              "status": 404,
                                              "detail": "Элемент с id=13213213 не найден",
                                              "instance": "/api/v1/ads"
                                            }
                                            """
                            )
                    )
            )
    })
    AdResponseDto createAd(@Validated({Default.class, CreateGroup.class}) AdCreateEditRequestDto adCreateEditRequestDto);

    AdResponseDto editAd(@Min(1) Long id, @Valid AdCreateEditRequestDto adCreateEditRequestDto);

    ResponseEntity<?> deleteAd(@Min(1) Long id);

    @SecurityRequirements
    AdResponseDto findById(@Min(1) Long id);
}
