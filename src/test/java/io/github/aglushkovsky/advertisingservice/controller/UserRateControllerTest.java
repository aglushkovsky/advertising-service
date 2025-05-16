package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.github.aglushkovsky.advertisingservice.service.UserRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.aglushkovsky.advertisingservice.util.UserRateTestUtils.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcUnitTest(UserRateController.class)
class UserRateControllerTest {

    @MockitoBean
    private UserRateService userRateService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserRateShouldCreateUserRateWhenAllParametersAreValid() throws Exception {
        Long recipientId = 1L;
        UserRateCreateRequestDto userRateCreateRequestDtoStub = createUserRateCreateRequestDtoStub();
        doReturn(mock(UserRateResponseDto.class)).when(userRateService).createUserRate(recipientId, userRateCreateRequestDtoStub);

        mockMvc.perform(post("/api/v1/users/{0}/rates", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRateCreateRequestDtoStub)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUserRateShouldReturnBadRequestResponseWhenRecipientIdIsInvalid() throws Exception {
        Long recipientId = 0L;
        UserRateCreateRequestDto userRateCreateRequestDtoStub = createUserRateCreateRequestDtoStub();

        mockMvc.perform(post("/api/v1/users/{0}/rates", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRateCreateRequestDtoStub)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.size()").value(1));
    }

    @Test
    void createUserRateShouldReturnBadRequestResponseWhenUserRateCreateRequestIsInvalid() throws Exception {
        Long recipientId = 1L;
        UserRateCreateRequestDto userRateCreateRequestDtoStub = UserRateCreateRequestDto.builder()
                .value(0.0)
                .build();

        mockMvc.perform(post("/api/v1/users/{0}/rates", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRateCreateRequestDtoStub)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.size()").value(1));
    }

    @Test
    void createUserRateShouldReturnBadRequestResponseWhenUserRateCreateRequestIsNull() throws Exception {
        Long recipientId = 1L;
        UserRateCreateRequestDto userRateCreateRequestDtoStub = UserRateCreateRequestDto.builder().build();

        mockMvc.perform(post("/api/v1/users/{0}/rates", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRateCreateRequestDtoStub)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.size()").value(1));
    }
}