package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdCrudService {

    private final AdDao adDao;

    private final AdMapper adMapper;

    public AdResponseDto findById(Long id) {
        log.info("Start findById; id={}", id);
        return adDao.findById(id).map(adMapper::toDto).orElseThrow(() -> {
            log.error("Could not find ad with id: {}", id);
            return new NotFoundException(id);
        });
    }
}
