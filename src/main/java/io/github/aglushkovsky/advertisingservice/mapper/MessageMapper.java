package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Message;
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
public abstract class MessageMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "author", source = "sender")
    public abstract MessageResponseDto toDto(Message message);

    @Mapping(target = "sender", expression = "java(userMapper.toUserFromAuthenticatedUserId())")
    @Mapping(target = "receiver", expression = "java(userMapper.toUserFromUserId(receiverId))")
    @Mapping(target = "sentAt", expression = "java(LocalDateTime.now())")
    public abstract Message toEntity(@Context Long receiverId, MessageCreateRequestDto requestDto);
}
