package io.github.aglushkovsky.advertisingservice.mapper.page;

import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.mapper.CommentMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.*;
import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(
        componentModel = SPRING,
        uses = CommentMapper.class,
        injectionStrategy = CONSTRUCTOR
)
public interface CommentPageMapper extends PageMapper<Comment, CommentResponseDto> {

}
