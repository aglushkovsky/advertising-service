package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.LocalityPart;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING)
@Slf4j
public abstract class LocalityMapper {

    @Autowired
    private LocalityDao localityDao;

    public abstract LocalityResponseDto toDto(Locality locality);

    public List<LocalityResponseDto> localityAncestorsToLocalityDtoList(Locality locality) {
        return locality.getAncestors()
                .stream()
                .sorted()
                .map(LocalityPart::getAncestorLocality)
                .map(this::toDto)
                .toList();
    }

    public Locality toEntityFromLocalityId(Long localityId) {
        return localityDao.findById(localityId).orElseThrow(() -> {
            log.error("Could not find locality with id={}", localityId);
            return new NotFoundException(localityId);
        });
    }
}
