package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.LocalityDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.LocalityPart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING)
public interface AdMapper {

    int DIGITS_AFTER_POINT = 2;

    @Mapping(target = "localityParts", source = "locality")
    AdResponseDto toDto(Ad ad);

    LocalityDto localityToLocalityDto(Locality locality);

    default BigDecimal convertToReadablePrice(BigDecimal price) {
        return price == null ? null : price.movePointLeft(DIGITS_AFTER_POINT);
    }

    default List<LocalityDto> localityAncestorsToLocalityDtoList(Locality locality) {
        return locality.getAncestors()
                .stream()
                .sorted()
                .map(LocalityPart::getAncestorLocality)
                .map(this::localityToLocalityDto)
                .toList();
    }
}
