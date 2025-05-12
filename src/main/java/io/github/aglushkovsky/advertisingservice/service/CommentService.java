package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.CommentDao;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.CommentMapper;
import io.github.aglushkovsky.advertisingservice.mapper.page.CommentPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommentService {

    private final CommentDao commentDao;
    private final AdDao adDao;
    private final CommentMapper commentMapper;
    private final CommentPageMapper commentPageMapper;

    public CommentResponseDto createComment(Long adId, CommentCreateRequestDto commentCreateRequestDto) {
        log.info("Creating comment for adId: {}", adId);

        Comment comment = commentMapper.toEntity(adId, commentCreateRequestDto);
        Comment createdComment = commentDao.add(comment);
        CommentResponseDto commentResponseDto = commentMapper.toDto(createdComment);

        log.info("Created comment for adId={} with id={}", adId, commentResponseDto.id());

        return commentResponseDto;
    }

    public PageEntity<CommentResponseDto> findAllCommentsByAdId(Long adId, PageableRequestDto pageable) {
        log.info("Finding all comments for adId: {}", adId);

        adDao.findById(adId).orElseThrow(() -> {
            log.error("Could not find ad with id: {}", adId);
            return new NotFoundException(adId);
        });

        PageEntity<Comment> commentPageEntity = commentDao.findAllByAdId(adId, pageable.limit(), pageable.page());
        PageEntity<CommentResponseDto> commentResponseDtoPageEntity = commentPageMapper.toDtoPage(commentPageEntity);

        log.info("Finished finding all comments for adId={} at page {}; found items: {}",
                adId, pageable.page(), commentResponseDtoPageEntity.body().size());

        return commentResponseDtoPageEntity;
    }
}
