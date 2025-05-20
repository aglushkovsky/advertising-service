package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.CommentDao;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.CommentMapper;
import io.github.aglushkovsky.advertisingservice.mapper.page.CommentPageMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.util.CommentTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.PageableTestCommonUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentDao commentDao;

    @Mock
    private AdDao adDao;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentPageMapper commentPageMapper;

    @InjectMocks
    private CommentService commentService;

    @Nested
    class CreateComment {

        @Test
        void createCommentShouldCreateCommentWhenAdIdIsValid() {
            Long adId = 1L;
            Long commentId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                    .text("Sample text")
                    .build();
            Comment commentStubWithoutId = createCommentStub(null);
            Comment createdCommentStubWithId = createCommentStub(commentId);
            CommentResponseDto commentResponseDtoStub = createCommentResponseDtoStub(commentId);
            doReturn(commentStubWithoutId).when(commentMapper).toEntity(adId, commentCreateRequestDto);
            doReturn(createdCommentStubWithId).when(commentDao).add(commentStubWithoutId);
            doReturn(commentResponseDtoStub).when(commentMapper).toDto(createdCommentStubWithId);

            CommentResponseDto result = commentService.createComment(adId, commentCreateRequestDto);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(commentResponseDtoStub);
            verify(commentMapper).toEntity(adId, commentCreateRequestDto);
            verify(commentDao).add(commentStubWithoutId);
            verify(commentMapper).toDto(createdCommentStubWithId);
            verifyNoInteractions(commentPageMapper);
            verifyNoInteractions(adDao);
        }
    }

    @Nested
    class FindAllCommentsByAdId {

        @Test
        void findAllCommentsByAdIdShouldReturnResultWhenAllParametersAreValid() {
            Long adId = 1L;
            Long commentId = 1L;
            PageableRequestDto pageable = createPageableRequestDto();
            PageEntity<Comment> pageCommentStub = createPageEntityStubWithSingleRecord(createCommentStub(commentId));
            PageEntity<CommentResponseDto> pageCommentResponseDtoStub = createPageEntityStubWithSingleRecord(
                    createCommentResponseDtoStub(commentId)
            );
            doReturn(Optional.of(new Ad())).when(adDao).findById(adId);
            doReturn(pageCommentStub).when(commentDao).findAllByAdId(adId, pageable.limit(), pageable.page());
            doReturn(pageCommentResponseDtoStub).when(commentPageMapper).toDtoPage(pageCommentStub);

            PageEntity<CommentResponseDto> result = commentService.findAllCommentsByAdId(adId, pageable);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(pageCommentResponseDtoStub);
            verify(adDao).findById(adId);
            verify(commentDao).findAllByAdId(adId, pageable.limit(), pageable.page());
            verify(commentPageMapper).toDtoPage(pageCommentStub);
            verifyNoInteractions(commentMapper);
        }

        @Test
        void findAllCommentsByAdIdShouldThrowExceptionWhenAdIdDoesNotExist() {
            Long adId = 1L;
            PageableRequestDto pageable = createPageableRequestDto();
            doReturn(Optional.empty()).when(adDao).findById(adId);

            assertThatThrownBy(() -> commentService.findAllCommentsByAdId(adId, pageable))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}