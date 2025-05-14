package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdPromoteService {

    private final AdCrudService adCrudService;

    public AdResponseDto promoteAd(Long adId) {
        log.info("Start promoteAd for ad with id={}", adId);

        AdResponseDto targetAd = adCrudService.findById(adId);

        if (!isUserAuthorizedForPromote(targetAd.publisher().id())) {
            log.error("User with id={} does not have permission to promote ad with id={}",
                    targetAd.publisher().id(), adId);
            throw new AccessDeniedException("You do not have permission to promote ad");
        }

        AdCreateEditResponseDto adCreateEditResponseDto = new AdCreateEditResponseDto(
                null,
                null,
                null,
                null,
                true,
                null
        );

        AdResponseDto adResponseDto = adCrudService.editAd(adId, adCreateEditResponseDto);

        log.info("Finished promoteAd successfully: ad with id={} is now promoted", adId);

        return adResponseDto;
    }

    private boolean isUserAuthorizedForPromote(Long adPublisherId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(Role.ADMIN) || authentication.getId().equals(adPublisherId);
    }
}
