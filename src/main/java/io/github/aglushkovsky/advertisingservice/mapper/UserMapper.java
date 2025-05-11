package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.util.MappingUtils;
import org.mapstruct.*;

import static org.mapstruct.InjectionStrategy.*;
import static org.mapstruct.MappingConstants.ComponentModel.*;
import static org.mapstruct.NullValuePropertyMappingStrategy.*;

@Mapper(
        componentModel = SPRING,
        uses = MappingUtils.class,
        injectionStrategy = CONSTRUCTOR
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAds", ignore = true)
    @Mapping(target = "incomingRates", ignore = true)
    @Mapping(target = "outgoingRates", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "totalRating", ignore = true)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = {"MappingUtils", "getPasswordHash"})
    User toUser(UserCreateEditRequestDto userCreateEditRequestDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAds", ignore = true)
    @Mapping(target = "incomingRates", ignore = true)
    @Mapping(target = "outgoingRates", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "totalRating", ignore = true)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = {"MappingUtils", "getPasswordHash"})
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    User updateUser(@MappingTarget User user, UserCreateEditRequestDto userCreateEditRequestDto);
}
