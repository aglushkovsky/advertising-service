package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.util.UserMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.MappingConstants.ComponentModel.*;
import static org.mapstruct.NullValuePropertyMappingStrategy.*;

@Mapper(
        componentModel = SPRING,
        uses = UserMapperUtils.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@Slf4j
public abstract class UserMapper {

    @Autowired
    protected UserDao userDao;

    @Mapping(target = "passwordHash", source = "password", qualifiedByName = {"UserMapperUtils", "getPasswordHash"})
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "totalRating", constant = "0.0")
    public abstract User toUser(UserCreateEditRequestDto userCreateEditRequestDto);

    public abstract UserResponseDto toDto(User user);

    @Mapping(target = "passwordHash", source = "password", qualifiedByName = {"UserMapperUtils", "getPasswordHash"})
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "totalRating", constant = "0.0")
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract User updateUser(@MappingTarget User user, UserCreateEditRequestDto userCreateEditRequestDto);

    public User toUserFromUserId(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new NotFoundException(userId);
                });
    }
}
