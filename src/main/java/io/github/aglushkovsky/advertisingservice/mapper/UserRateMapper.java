package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.mapstruct.MappingConstants.ComponentModel.*;
import static org.mapstruct.ReportingPolicy.*;

@Mapper(
        componentModel = SPRING,
        uses = UserMapper.class,
        imports = LocalDateTime.class,
        unmappedTargetPolicy = IGNORE
)
public abstract class UserRateMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "author", expression = "java(userMapper.toUserFromAuthenticatedUserId())")
    @Mapping(target = "recipient", expression = "java(userMapper.toUserFromUserId(recipientId))")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    public abstract UserRate toEntity(@Context Long recipientId, UserRateCreateRequestDto userRateCreateRequestDto);
    
    public abstract UserRateResponseDto toDto(UserRate userRate);
}
