package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.mapper.LocalityMapper;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ExistsIdInLocalityDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class LocalityService {

    private final LocalityDao localityDao;

    private final LocalityMapper localityMapper;

    public List<LocalityResponseDto> findAllByLocalityType(LocalityType type) {
        log.info("Start findAllByLocalityType; type={}", type);

        List<Locality> result = localityDao.findAllByLocalityType(type);

        log.info("Finished findAllByLocalityType; type={}, found items: {}", type, result.size());

        return result.stream().map(localityMapper::toDto).toList();
    }

    public List<LocalityResponseDto> findDirectDescendantsByLocalityId(@ExistsIdInLocalityDao Long localityId) {
        log.info("Start findDirectDescendantsByLocalityId; localityId={}", localityId);

        List<Locality> result = localityDao.findDirectDescendantsByLocalityId(localityId);

        log.info("Finished findDirectDescendantsByLocalityId; localityId={}, found items: {}", localityId, result.size());

        return result.stream().map(localityMapper::toDto).toList();
    }

    public List<String> findAllAvailableLocalityTypes() {
        log.info("Start findAllAvailableLocalityTypes");
        List<String> result = Arrays.stream(LocalityType.values()).map(LocalityType::name).toList();
        log.info("Finished findAllAvailableLocalityTypes");
        return result;
    }
}
