package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.CommentDao;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.mapper.CommentMapper;
import io.github.aglushkovsky.advertisingservice.mapper.page.CommentPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentDao commentDao;
    private final CommentMapper commentMapper;
    private final CommentPageMapper commentPageMapper;

    public CommentResponseDto createComment(Long adId, CommentCreateRequestDto commentCreateRequestDto) {
        Comment comment = commentMapper.toEntity(adId, commentCreateRequestDto);
        Comment createdComment = commentDao.add(comment);
        return commentMapper.toDto(createdComment);
    }

    public PageEntity<CommentResponseDto> findAllCommentsByAdId(Long adId, PageableRequestDto pageable) {
        PageEntity<Comment> commentPageEntity = commentDao.findAllByAdId(adId, pageable.limit(), pageable.page());
        return commentPageMapper.toDtoPage(commentPageEntity);
    }
}
