package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.util.SecurityUtils;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(
        componentModel = SPRING,
        uses = {UserMapper.class, AdMapper.class},
        imports = {SecurityUtils.class, LocalDateTime.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CommentMapper {

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected AdMapper adMapper;

    @Mapping(target = "author", expression = "java(userMapper.toUserFromUserId(SecurityUtils.getAuthenticatedUserId()))")
    @Mapping(target = "ad", expression = "java(adMapper.toEntityFromAdId(adId))")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    public abstract Comment toEntity(@Context Long adId, CommentCreateRequestDto commentCreateRequestDto);

    public abstract CommentResponseDto toDto(Comment comment);
}
