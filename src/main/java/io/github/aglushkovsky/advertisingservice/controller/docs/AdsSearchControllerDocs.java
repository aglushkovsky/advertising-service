package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

@SecurityRequirements
@Tag(name = "Ads Search", description = "Operations for ads search")
public interface AdsSearchControllerDocs {

    PageEntity<AdResponseDto> searchAds(@ParameterObject FindAllAdsFilterRequestDto filter,
                                        @ParameterObject PageableRequestDto pageable);

    PageEntity<AdResponseDto> getAdsHistoryByUserId(Long userId, @ParameterObject PageableRequestDto pageable);
}
