package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.ScrollableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.service.MessageService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.github.aglushkovsky.advertisingservice.util.MessageTestUtils.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcUnitTest(MessageController.class)
class MessageControllerTest {

    @MockitoBean
    private MessageService messageService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SendMessage {

        @Test
        void sendMessageShouldSendMessageWhenAllParametersAreValid() throws Exception {
            Long receiverId = 1L;
            MessageCreateRequestDto messageRequestDtoStub = createMessageRequestDtoStub();
            MessageResponseDto messageResponseDtoStub = createMessageResponseDtoStub(1L);
            doReturn(messageResponseDtoStub).when(messageService).sendMessage(receiverId, messageRequestDtoStub);

            mockMvc.perform(post("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(messageRequestDtoStub)))
                    .andExpect(status().isCreated());
        }

        @Test
        void sendMessageShouldReturnBadRequestResponseWhenReceiverIdIsInvalid() throws Exception {
            Long receiverId = 0L;
            MessageCreateRequestDto messageRequestDtoStub = createMessageRequestDtoStub();

            mockMvc.perform(post("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(messageRequestDtoStub)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }

        @Test
        void sendMessagesShouldReturnBadRequestResponseWhenMessageCreateRequestIsInvalid() throws Exception {
            Long receiverId = 1L;
            MessageCreateRequestDto messageRequestDtoStub = MessageCreateRequestDto.builder()
                    .text("test")
                    .build();

            mockMvc.perform(post("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(messageRequestDtoStub)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }

        @Test
        void sendMessagesShouldReturnBadRequestResponseWhenMessageTextIsNull() throws Exception {
            Long receiverId = 1L;
            MessageCreateRequestDto messageRequestDtoStub = MessageCreateRequestDto.builder().build();

            mockMvc.perform(post("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(messageRequestDtoStub)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }
    }

    @Nested
    class FindMessages {

        @Test
        void findMessagesShouldReturnMessagesWhenAllParametersAreValid() throws Exception {
            Long receiverId = 1L;
            ScrollableRequestDto scrollable = ScrollableRequestDto.builder()
                    .startId(1L)
                    .limit(10L)
                    .scrollDirection(ScrollDirection.DOWN)
                    .build();
            List<MessageResponseDto> messageResponseDtoListStub = List.of(createMessageResponseDtoStub(1L));
            doReturn(messageResponseDtoListStub).when(messageService).findMessages(receiverId, scrollable);

            mockMvc.perform(get("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .queryParam("limit", String.valueOf(scrollable.limit()))
                            .queryParam("scrollDirection", scrollable.scrollDirection().name())
                            .queryParam("startId", String.valueOf(scrollable.startId())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void findMessagesShouldReturnBadRequestResponseWhenReceiverIdIsInvalid() throws Exception {
            Long receiverId = 0L;
            ScrollableRequestDto scrollable = ScrollableRequestDto.builder()
                    .startId(1L)
                    .limit(10L)
                    .scrollDirection(ScrollDirection.DOWN)
                    .build();

            mockMvc.perform(get("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .queryParam("limit", String.valueOf(scrollable.limit()))
                            .queryParam("scrollDirection", scrollable.scrollDirection().name())
                            .queryParam("startId", String.valueOf(scrollable.startId())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }

        @Test
        void findMessagesShouldReturnBadRequestResponseWhenScrollDirectionIsInvalid() throws Exception {
            Long receiverId = 1L;

            mockMvc.perform(get("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .queryParam("scrollDirection", "INVALID_DIRECTION"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }

        @Test
        void findMessagesShouldReturnBadRequestResponseWhenScrollableDirectionIsInvalidButOtherParametersAreInvalid() throws Exception {
            Long receiverId = 1L;
            ScrollableRequestDto scrollable = ScrollableRequestDto.builder()
                    .startId(0L)
                    .limit(0L)
                    .build();

            mockMvc.perform(get("/api/v1/messages")
                            .queryParam("receiverId", String.valueOf(receiverId))
                            .queryParam("startId", String.valueOf(scrollable.startId()))
                            .queryParam("limit", String.valueOf(scrollable.limit())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(2));
        }
    }
}