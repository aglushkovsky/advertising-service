package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.CommentDao;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import io.github.aglushkovsky.advertisingservice.mapper.UserMapper;
import io.github.aglushkovsky.advertisingservice.test.config.MapperTestConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType.*;
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.Role.*;
import static io.github.aglushkovsky.advertisingservice.util.SecurityUtils.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO Подумать, как можно отрефакторить тесты, чтобы было меньше кода.
@SpringJUnitConfig(CommentService.class)
@Import(MapperTestConfig.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private AdMapper adMapper;

    @MockitoBean
    private CommentDao commentDao;

    @MockitoBean
    private AdDao adDao;

    @Nested
    class CreateComment {

        @Test
        void createCommentShouldCreateCommentWhenAdIdIsValid() {
            Long adId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("test comment");
            User user = new User(
                    1L,
                    "test_user",
                    "password_hash",
                    null,
                    null,
                    USER,
                    0.0,
                    emptyList(),
                    emptyList(),
                    emptyList()
            );
            UserResponseDto userResponseDtoStub = new UserResponseDto(
                    1L,
                    "test_user",
                    null,
                    null,
                    0.0
            );
            LocalDateTime createdAt = LocalDateTime.parse("2025-05-12T13:51:48.899862700");
            Ad adStub = new Ad(
                    1L,
                    "ad title",
                    new BigDecimal("12345"),
                    null,
                    new Locality(1L, "Test City", emptyList(), emptyList(), CITY),
                    user,
                    createdAt,
                    false
            );
            Comment createdCommentStub = new Comment(
                    1L,
                    user,
                    adStub,
                    createdAt,
                    "test comment"
            );
            doReturn(user).when(userMapper).toUserFromUserId(getAuthenticatedUserId());
            doReturn(userResponseDtoStub).when(userMapper).toDto(user);
            doReturn(adStub).when(adMapper).toEntityFromAdId(adId);
            doReturn(createdCommentStub).when(commentDao).add(any(Comment.class));

            CommentResponseDto comment = commentService.createComment(adId, commentCreateRequestDto);

            assertThat(comment.id()).isEqualTo(adId);
            assertThat(comment.author().id()).isEqualTo(user.getId());
            assertThat(comment.createdAt()).isEqualTo(createdAt);
            assertThat(comment.text()).isEqualTo(commentCreateRequestDto.text());
            verify(commentDao).add(any(Comment.class));
        }

        @Test
        void createCommentShouldCreateCommentWhenAdIdIsInvalid() {
            Long invalidAdId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("test comment");
            doThrow(NotFoundException.class).when(adMapper).toEntityFromAdId(invalidAdId);

            assertThatThrownBy(() -> commentService.createComment(invalidAdId, commentCreateRequestDto))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class FindAllByAdId {

        @Test
        void findAllShouldReturnResultWhenAdIdIsValid() {
            Long adId = 1L;
            PageableRequestDto pageableRequestDto = new PageableRequestDto(10L, 1L);
            User user = new User(
                    1L,
                    "test_user",
                    "password_hash",
                    null,
                    null,
                    USER,
                    0.0,
                    emptyList(),
                    emptyList(),
                    emptyList()
            );
            LocalDateTime createdAt = LocalDateTime.parse("2025-05-12T13:51:48.899862700");
            Ad adStub = new Ad(
                    1L,
                    "ad title",
                    new BigDecimal("12345"),
                    null,
                    new Locality(1L, "Test City", emptyList(), emptyList(), CITY),
                    user,
                    createdAt,
                    false
            );
            Comment commentStub = new Comment(
                    1L,
                    user,
                    adStub,
                    createdAt,
                    "test comment"
            );
            PageEntity<Comment> commentPageEntity = new PageEntity<>(
                    List.of(commentStub),
                    new PageEntity.Metadata(1L, 1L, 1L, true)
            );
            doReturn(commentPageEntity).when(commentDao).findAllByAdId(adId,
                    pageableRequestDto.limit(), pageableRequestDto.page());
            doReturn(Optional.of(adStub)).when(adDao).findById(adId);

            PageEntity<CommentResponseDto> comments = commentService.findAllCommentsByAdId(adId, pageableRequestDto);

            assertThat(comments.body()).hasSize(1);
            assertThat(comments.metadata().currentPage()).isEqualTo(1L);
            assertThat(comments.metadata().totalPages()).isEqualTo(1L);
            assertThat(comments.metadata().totalRecords()).isEqualTo(1L);
            assertThat(comments.metadata().isLastPage()).isTrue();
            verify(commentDao).findAllByAdId(adId, pageableRequestDto.limit(), pageableRequestDto.page());
        }

        @Test
        void findAllShouldReturnResultWhenAdIdIsInvalid() {
            Long invalidAdId = 1L;
            PageableRequestDto pageableRequestDto = new PageableRequestDto(10L, 1L);
            doThrow(NotFoundException.class).when(adDao).findById(invalidAdId);

            assertThatThrownBy(() -> commentService.findAllCommentsByAdId(invalidAdId, pageableRequestDto))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}