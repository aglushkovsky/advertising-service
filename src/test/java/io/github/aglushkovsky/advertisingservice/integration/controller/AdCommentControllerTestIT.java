package io.github.aglushkovsky.advertisingservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.github.aglushkovsky.advertisingservice.dao.impl.CommentDao;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static io.github.aglushkovsky.advertisingservice.util.CommentTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdCommentControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentDao commentDao;

    @Nested
    class CreateCommentForAd {

        @Test
        void createCommentForAdShouldCreateCommentWhenAdIdExists() throws Exception {
            Long adId = 1L;
            CommentCreateRequestDto commentCreateRequestDto = createCommentCreateRequestDto();

            MvcResult result = mockMvc.perform(post("/api/v1/ads/{0}/comments", adId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            Integer id = JsonPath.parse(contentAsString).read("$.id");

            Comment createdComment = commentDao.findById(id.longValue()).orElseThrow();
            assertThat(createdComment.getId()).isEqualTo(id.longValue());
            assertThat(createdComment.getText()).isEqualTo(commentCreateRequestDto.text());
        }

        @Test
        void createCommentForAdShouldReturnNotFoundWhenAdIdDoesNotExists() throws Exception {
            Long nonExistentAdId = 10000000L;
            CommentCreateRequestDto commentCreateRequestDto = createCommentCreateRequestDto();

            mockMvc.perform(post("/api/v1/ads/{0}/comments", nonExistentAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class FindAllCommentsByAdId {

        @Test
        void findAllCommentsByAdIdShouldAllAdCommentsWhenAdIdExists() throws Exception {
            Long adId = 1L;

            mockMvc.perform(get("/api/v1/ads/{0}/comments", adId))
                    .andExpect(status().isOk());
        }

        @Test
        void findAllCommentsByAdIdShouldReturnNotFoundWhenAdIdDoesNotExists() throws Exception {
            Long nonExistentAdId = 10000000L;

            mockMvc.perform(get("/api/v1/ads/{0}/comments", nonExistentAdId))
                    .andExpect(status().isNotFound());
        }
    }
}
