package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

@Tag(name = "Ad promoting")
public interface AdPromoteControllerDocs {

    AdResponseDto promoteAd(@Min(1) Long id);
}
