package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;

@SecurityRequirements
@Tag(name = "Ads Search", description = "Operations for ads search")
public interface AdsSearchControllerDocs {

    PageEntity<AdResponseDto> searchAds(@ParameterObject @Valid FindAllAdsFilterRequestDto filter,
                                        @ParameterObject @Valid PageableRequestDto pageable);

    PageEntity<AdResponseDto> getAdsHistoryByUserId(@Min(1) Long userId,
                                                    @ParameterObject @Valid PageableRequestDto pageable);
}
