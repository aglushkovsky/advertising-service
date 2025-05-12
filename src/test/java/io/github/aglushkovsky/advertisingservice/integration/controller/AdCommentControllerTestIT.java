package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// TODO Написать тесты
public class AdCommentControllerTestIT extends AbstractIntegrationTest {

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
