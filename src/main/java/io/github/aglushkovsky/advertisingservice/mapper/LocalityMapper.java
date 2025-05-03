package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.LocalityDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.LocalityPart;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING)
public interface LocalityMapper {

    LocalityDto toDto(Locality locality);

    default List<LocalityDto> localityAncestorsToLocalityDtoList(Locality locality) {
        return locality.getAncestors()
                .stream()
                .sorted()
                .map(LocalityPart::getAncestorLocality)
                .map(this::toDto)
                .toList();
    }
}
