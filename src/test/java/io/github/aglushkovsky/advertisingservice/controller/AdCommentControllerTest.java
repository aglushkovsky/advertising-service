package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.service.CommentService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// TODO Написать тесты
@WebMvcUnitTest(AdCommentController.class)
class AdCommentControllerTest {

    @MockitoBean
    private CommentService commentService;

    @Nested
    class CreateCommentForAd {

        @Test
        void createCommentForAdShouldCreateCommentWhenAdIdExists() {

        }

        @Test
        void createCommentForAdShouldReturnNotFoundWhenAdIdDoesNotExists() {

        }

        @Test
        void createCommentForAdShouldReturnBadRequestWhenAdIdIsInvalid() {

        }

        @Test
        void createCommentForAdShouldReturnBadRequestWhenCommentTextSizeIsLessThanFiveCharacters() {

        }
    }

    @Nested
    class FindAllCommentsByAdId {

        @Test
        void findAllCommentsByAdIdShouldAllAdCommentsWhenAdIdExists() {
            
        }

        @Test
        void findAllCommentsByAdIdShouldReturnNotFoundWhenAdIdDoesNotExists() {

        }

        @Test
        void findAllCommentsByAdIdShouldReturnAllAdCommentsWhenPageAndLimitAreValid() {

        }

        @Test
        void findAllCommentsByAdIdShouldReturnBadRequestWhenPageAndLimitAreInvalid() {

        }
    }
}