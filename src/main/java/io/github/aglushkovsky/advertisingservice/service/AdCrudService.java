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
// TODO Добавить логирование!
public class AdCrudService {

    private final AdDao adDao;

    private final AdMapper adMapper;

    // FIXME некорректный маппинг цены, надо пофиксить
    @Transactional
    public AdResponseDto createAd(AdCreateEditResponseDto adCreateEditResponseDto) {
        Ad ad = adMapper.toEntity(adCreateEditResponseDto);
        Ad createdAd = adDao.add(ad);
        return adMapper.toDto(createdAd);
    }

    @Transactional
    public AdResponseDto editAd(Long adId, AdCreateEditResponseDto adCreateEditResponseDto) {
        Ad adForUpdate = adDao.findById(adId).orElseThrow(() -> {
            return new NotFoundException(adId);
        });

        if (!isUserAuthorizedForAction(adForUpdate.getPublisher().getId())) {
            throw new AccessDeniedException("User is not authorized to edit ad with id=%d".formatted(adId));
        }

        Ad updatedAd = adMapper.updateAd(adForUpdate, adCreateEditResponseDto);
        adDao.update(updatedAd);
        AdResponseDto adResponseDto = adMapper.toDto(updatedAd);

        return adResponseDto;
    }

    @Transactional
    public void deleteAd(Long adId) {
        Ad adForDelete = adDao.findById(adId).orElseThrow(() -> {
            return new NotFoundException(adId);
        });

        if (!isUserAuthorizedForAction(adForDelete.getPublisher().getId())) {
            throw new AccessDeniedException("User is not authorized to delete ad with id=%d".formatted(adId));
        }

        adDao.delete(adForDelete);
    }

    private boolean isUserAuthorizedForAction(Long adPublisherId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(Role.ADMIN) ||
               Objects.equals(authentication.getId(), adPublisherId);
    }

    public AdResponseDto findById(Long id) {
        log.info("Start findById; id={}", id);
        return adDao.findById(id).map(adMapper::toDto).orElseThrow(() -> {
            log.error("Could not find ad with id: {}", id);
            return new NotFoundException(id);
        });
    }
}
