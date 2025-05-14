package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdCrudService {

    private final AdDao adDao;
    private final AdMapper adMapper;

    @Transactional
    public AdResponseDto createAd(AdCreateEditResponseDto adCreateEditResponseDto) {
        log.info("Creating new Ad; adCreateEditResponseDto: {}", adCreateEditResponseDto);

        Ad ad = adMapper.toEntity(adCreateEditResponseDto);
        Ad createdAd = adDao.add(ad);
        AdResponseDto adResponseDto = adMapper.toDto(createdAd);

        log.info("Created ad with id={}", adResponseDto.id());

        return adResponseDto;
    }

    @Transactional
    public AdResponseDto editAd(Long adId, AdCreateEditResponseDto adCreateEditResponseDto) {
        log.info("Editing Ad; id={}", adId);

        Ad adForUpdate = adDao.findById(adId).orElseThrow(() -> {
            log.error("Can't edit: ad with id={} not found", adId);
            return new NotFoundException(adId);
        });

        if (isUserNotAuthorizedForAction(adForUpdate.getPublisher().getId())) {
            log.error("User is not authorized for editing ad with id={}", adId);
            throw new AccessDeniedException("User is not authorized to edit ad with id=%d".formatted(adId));
        }

        Ad updatedAd = adMapper.editAd(adForUpdate, adCreateEditResponseDto);
        adDao.update(updatedAd);
        AdResponseDto adResponseDto = adMapper.toDto(updatedAd);

        log.info("Edited ad with id={}", adResponseDto.id());

        return adResponseDto;
    }

    @Transactional
    public void deleteAd(Long adId) {
        log.info("Deleting Ad; id={}", adId);

        Ad adForDelete = adDao.findById(adId).orElseThrow(() -> {
            log.error("Can't delete: ad with id={} not found", adId);
            return new NotFoundException(adId);
        });

        if (isUserNotAuthorizedForAction(adForDelete.getPublisher().getId())) {
            throw new AccessDeniedException("User is not authorized to delete ad with id=%d".formatted(adId));
        }

        adDao.delete(adForDelete);

        log.info("Deleted ad with id={}", adId);
    }

    private boolean isUserNotAuthorizedForAction(Long adPublisherId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return !authentication.getAuthorities().contains(Role.ADMIN) &&
               !Objects.equals(authentication.getId(), adPublisherId);
    }

    public AdResponseDto findById(Long id) {
        log.info("Start findById; id={}", id);
        return adDao.findById(id).map(adMapper::toDto).orElseThrow(() -> {
            log.error("Could not find ad with id: {}", id);
            return new NotFoundException(id);
        });
    }
}
