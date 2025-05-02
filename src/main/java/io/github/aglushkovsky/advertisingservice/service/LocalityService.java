package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dto.LocalityDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.LocalityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalityService {

    private final LocalityDao localityDao;

    private final LocalityMapper localityMapper;

    public List<LocalityDto> findAllByLocalityType(String type) {
        log.info("Start findAllByLocalityType; type={}", type);

        LocalityType localityType = LocalityType.valueOf(type);
        List<Locality> result = localityDao.findAllByLocalityType(localityType);

        log.info("Finished findAllByLocalityType; type={}, found items: {}", type, result.size());

        return result
                .stream()
                .map(localityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LocalityDto> findDirectDescendantsByLocalityId(Long localityId) {
        log.info("Start findDirectDescendantsByLocalityId; localityId={}", localityId);

        if (!localityDao.isExists(localityId)) {
            log.error("Could not find locality with id={}", localityId);
            throw new NotFoundException(localityId);
        }

        List<Locality> result = localityDao.findDirectDescendantsByLocalityId(localityId);

        log.info("Finished findDirectDescendantsByLocalityId; localityId={}, found items: {}", localityId, result.size());

        return result
                .stream()
                .map(localityMapper::toDto)
                .toList();
    }

    // TODO реализовать метод для получения доступных типов локалей.
}
