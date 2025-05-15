package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.service.CommentService;
import io.github.aglushkovsky.advertisingservice.util.PageableTestCommonUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.aglushkovsky.advertisingservice.util.CommentTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.PageableTestCommonUtils.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcUnitTest(AdCommentController.class)
class AdCommentControllerTest {

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CreateCommentForAd {

        @Test
        void createCommentForAdShouldCreateCommentWhenAdIdExists() throws Exception {
            Long targetAdId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = createCommentCreateRequestDto();
            CommentResponseDto commentResponseDtoStub = createCommentResponseDtoStub(1L);
            doReturn(commentResponseDtoStub).when(commentService).createComment(targetAdId, commentCreateRequestDto);

            mockMvc.perform(post("/api/v1/ads/{0}/comments", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(commentResponseDtoStub)));
        }

        @Test
        void createCommentForAdShouldReturnNotFoundWhenAdIdDoesNotExists() throws Exception {
            Long targetAdId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = createCommentCreateRequestDto();
            doThrow(NotFoundException.class).when(commentService).createComment(targetAdId, commentCreateRequestDto);

            mockMvc.perform(post("/api/v1/ads/{0}/comments", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        void createCommentForAdShouldReturnBadRequestWhenAdIdIsInvalid() throws Exception {
            Long targetAdId = 0L;
            CommentCreateRequestDto commentCreateRequestDto = createCommentCreateRequestDto();

            mockMvc.perform(post("/api/v1/ads/{0}/comments", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        void createCommentForAdShouldReturnBadRequestWhenCommentTextSizeIsLessThanFiveCharacters() throws Exception {
            Long targetAdId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                    .text("t")
                    .build();

            mockMvc.perform(post("/api/v1/ads/{0}/comments", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
        }
    }

    // FIXME Накосячил в тестах с передачей pageable. В mockMvc их надо передавать в queryParam (собственно, как и в реальной жизни).
    @Nested
    class FindAllCommentsByAdId {

        @Test
        void findAllCommentsByAdIdShouldAllAdCommentsWhenAdIdExists() throws Exception {
            Long adId = 1L;
            PageEntity<CommentResponseDto> pageCommentResponseDto = createPageEntityStubWithSingleRecord(mock(CommentResponseDto.class));
            doReturn(pageCommentResponseDto).when(commentService).findAllCommentsByAdId(eq(adId), any(PageableRequestDto.class));

            mockMvc.perform(get("/api/v1/ads/{adId}/comments", adId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(pageCommentResponseDto)));

        }

        @Test
        void findAllCommentsByAdIdShouldReturnNotFoundWhenAdIdDoesNotExists() throws Exception {
            Long adId = 1L;
            doThrow(NotFoundException.class).when(commentService).findAllCommentsByAdId(eq(adId), any(PageableRequestDto.class));

            mockMvc.perform(get("/api/v1/ads/{adId}/comments", adId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        void findAllCommentsByAdIdShouldReturnAllAdCommentsWhenPageAndLimitAreValid() throws Exception {
            Long adId = 1L;
            PageableRequestDto pageableRequestDto = createPageableRequestDto();
            PageEntity<CommentResponseDto> pageCommentResponseDto = createPageEntityStubWithSingleRecord(mock(CommentResponseDto.class));
            doReturn(pageCommentResponseDto).when(commentService).findAllCommentsByAdId(adId, pageableRequestDto);

            mockMvc.perform(get("/api/v1/ads/{adId}/comments", adId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(pageCommentResponseDto)));
        }

        @Test
        void findAllCommentsByAdIdShouldReturnBadRequestWhenPageAndLimitAreInvalid() {

        }
    }
}