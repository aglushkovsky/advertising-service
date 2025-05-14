package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.service.AdCrudService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcUnitTest(AdCrudController.class)
class AdCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdCrudService adCrudService;

//    @Nested
//    class FindById {
//
//        @Test
//        void findByIdShouldReturnItemWhenItExistsInDao() throws Exception {
//            Long adStubId = 1L;
//            AdResponseDto findByIdStub = new AdResponseDto(
//                    adStubId,
//                    "Test",
//                    new BigDecimal("12345"),
//                    null,
//                    List.of(new LocalityResponseDto(1L, "Test City", "CITY")),
//                    new UserResponseDto(1L, "test_login", null, null, 0.0),
//                    LocalDateTime.now().toString(),
//                    false);
//            doReturn(findByIdStub).when(adCrudService).findById(anyLong());
//
//            mockMvc.perform(get("/api/v1/ads/{id}", adStubId))
//                    .andExpect(status().isOk())
//                    .andExpect(content().json(objectMapper.writeValueAsString(findByIdStub)));
//        }
//
//        @Test
//        void findByIdShouldReturnNotFoundResponseWhenItDoesNotExist() throws Exception {
//            Long adRequestId = 1L;
//            doThrow(NotFoundException.class).when(adCrudService).findById(adRequestId);
//
//            mockMvc.perform(get("/api/v1/ads/{id}", adRequestId))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        void findByIdShouldReturnBadRequestResponseWhenIdIsInvalid() throws Exception {
//            Long invalidAdRequestId = 0L;
//            doThrow(NotFoundException.class).when(adCrudService).findById(invalidAdRequestId);
//
//            mockMvc.perform(get("/api/v1/ads/{id}", invalidAdRequestId))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.body.size()").value(1))
//                    .andExpect(jsonPath("$.body[?(@.parameter=='id')]", notNullValue()));
//        }
//    }
}