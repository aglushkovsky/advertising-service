package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dto.LocalityDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.mapper.LocalityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO Как это покрыть тестами?
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

    // TODO То же самое. Нужно ли валидировать переданный id и кидать исключение, если такого localityId не существует?
    public List<LocalityDto> findDirectDescendantsByLocalityId(Long localityId) {
        log.info("Start findDirectDescendantsByLocalityId; localityId={}", localityId);

        List<Locality> result = localityDao.findDirectDescendantsByLocalityId(localityId);

        log.info("Finished findDirectDescendantsByLocalityId; localityId={}, found items: {}", localityId, result.size());

        return result
                .stream()
                .map(localityMapper::toDto)
                .toList();
    }
}
