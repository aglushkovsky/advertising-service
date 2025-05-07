package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.LocalityPart;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING)
public interface LocalityMapper {

    LocalityResponseDto toDto(Locality locality);

    default List<LocalityResponseDto> localityAncestorsToLocalityDtoList(Locality locality) {
        return locality.getAncestors()
                .stream()
                .sorted()
                .map(LocalityPart::getAncestorLocality)
                .map(this::toDto)
                .toList();
    }
}
