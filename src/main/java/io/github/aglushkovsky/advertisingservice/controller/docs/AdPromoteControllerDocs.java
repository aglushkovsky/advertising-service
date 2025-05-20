package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ad promoting")
public interface AdPromoteControllerDocs {

    AdResponseDto promoteAd(Long id);
}
