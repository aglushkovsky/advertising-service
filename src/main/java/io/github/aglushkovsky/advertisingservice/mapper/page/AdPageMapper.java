package io.github.aglushkovsky.advertisingservice.mapper.page;

import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.*;
import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(
        componentModel = SPRING,
        uses = AdMapper.class,
        injectionStrategy = CONSTRUCTOR
)
public interface AdPageMapper extends PageMapper<Ad, AdResponseDto> {

}
