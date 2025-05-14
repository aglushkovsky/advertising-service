package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static io.github.aglushkovsky.advertisingservice.util.AdServicesTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.UserServiceTestUtils.*;

@UtilityClass
public class CommentServiceTestUtils {

    public static Comment createCommentStub(Long commentId) {
        return Comment.builder()
                .id(commentId)
                .author(createUserStub())
                .ad(createAdStub(1L))
                .createdAt(LocalDateTime.now())
                .text("Sample text")
                .build();
    }

    public static CommentResponseDto createCommentResponseDtoStub(Long commentId) {
        return CommentResponseDto.builder()
                .id(commentId)
                .author(createUserResponseDtoStub())
                .createdAt(LocalDateTime.now())
                .text("Sample text")
                .build();
    }
}
