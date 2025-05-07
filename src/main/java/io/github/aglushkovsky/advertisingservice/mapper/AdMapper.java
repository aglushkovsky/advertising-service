package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

import static org.mapstruct.InjectionStrategy.*;
import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING,
        uses = LocalityMapper.class,
        injectionStrategy = CONSTRUCTOR)
public interface AdMapper {

    int DIGITS_AFTER_POINT = 2;

    @Mapping(target = "localityParts", source = "locality")
    AdResponseDto toDto(Ad ad);

    default BigDecimal convertToReadablePrice(BigDecimal price) {
        return price == null ? null : price.movePointLeft(DIGITS_AFTER_POINT);
    }
}
