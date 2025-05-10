package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.PublisherResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.service.AdSearchService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcUnitTest(AdSearchController.class)
class AdSearchControllerTest {

    @MockitoBean
    private AdSearchService adSearchService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SearchAds {

        @Test
        void searchAdsShouldReturnFirstPageFromAllLocalitiesWhenFilterAndPageableNotSpecified() throws Exception {
            PageEntity<AdResponseDto> findAllResultStub = new PageEntity<>(
                    List.of(
                            new AdResponseDto(
                                    1L,
                                    "Test",
                                    new BigDecimal("12345"),
                                    null,
                                    List.of(new LocalityResponseDto(1L, "Test City", "CITY")),
                                    new PublisherResponseDto(1L, "test_login", null, null, 0.0),
                                    LocalDateTime.now().toString(),
                                    false)
                    ),
                    new PageEntity.Metadata(1L, 1L, 1L, true)
            );
            doReturn(findAllResultStub).when(adSearchService).findAll(any(), any());

            mockMvc.perform(get("/api/v1/ads"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(findAllResultStub)));
        }

        @Test
        void searchAdsShouldReturnFirstPageFromAllLocalitiesWhenFilterAndPageableParametersWasSpecified() throws Exception {
            PageEntity<AdResponseDto> findAllResultStub = new PageEntity<>(
                    List.of(
                            new AdResponseDto(
                                    1L,
                                    "Test",
                                    new BigDecimal("12345"),
                                    null,
                                    List.of(new LocalityResponseDto(1L, "Test City", "CITY")),
                                    new PublisherResponseDto(1L, "test_login", null, null, 0.0),
                                    LocalDateTime.now().toString(),
                                    false)
                    ),
                    new PageEntity.Metadata(1L, 1L, 1L, true)
            );
            doReturn(findAllResultStub).when(adSearchService).findAll(any(), any());

            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("term", "test")
                            .queryParam("onlyInTitle", "false")
                            .queryParam("minPrice", "10000")
                            .queryParam("maxPrice", "100000")
                            .queryParam("publisherId", "1")
                            .queryParam("localityId", "1")
                            .queryParam("page", "1")
                            .queryParam("limit", "10"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(findAllResultStub)));
        }

        @Test
        void searchAdsShouldReturnErrorResponseWhenFilterParametersAreInInvalidFormat() throws Exception {
            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("publisherId", "-1")
                            .queryParam("localityId", "-1")
                            .queryParam("minPrice", "-10")
                            .queryParam("maxPrice", "-20"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.body.size()").value(5))
                    .andExpect(jsonPath("$.body[*].parameter",
                            hasItems("publisherId", "localityId", "minPrice", "maxPrice", null)));
        }

        @Test
        void searchAdsShouldReturnErrorResponseWhenPageableParametersAreInInvalidFormat() throws Exception {
            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("page", "0")
                            .queryParam("limit", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.body.size()").value(2))
                    .andExpect(jsonPath("$.body[?(@.parameter=='page')]", notNullValue()))
                    .andExpect(jsonPath("$.body[?(@.parameter=='limit')]", notNullValue()));
        }
    }

    @Nested
    class FindById {

        @Test
        void findByIdShouldReturnItemWhenItExistsInDao() throws Exception {
            Long adStubId = 1L;
            AdResponseDto findByIdStub = new AdResponseDto(
                    adStubId,
                    "Test",
                    new BigDecimal("12345"),
                    null,
                    List.of(new LocalityResponseDto(1L, "Test City", "CITY")),
                    new PublisherResponseDto(1L, "test_login", null, null, 0.0),
                    LocalDateTime.now().toString(),
                    false);
            doReturn(findByIdStub).when(adSearchService).findById(anyLong());

            mockMvc.perform(get("/api/v1/ads/{id}", adStubId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(findByIdStub)));
        }

        @Test
        void findByIdShouldReturnNotFoundResponseWhenItDoesNotExist() throws Exception {
            Long adRequestId = 1L;
            doThrow(NotFoundException.class).when(adSearchService).findById(adRequestId);

            mockMvc.perform(get("/api/v1/ads/{id}", adRequestId))
                    .andExpect(status().isNotFound());
        }

        @Test
        void findByIdShouldReturnBadRequestResponseWhenIdIsInvalid() throws Exception {
            Long invalidAdRequestId = 0L;
            doThrow(NotFoundException.class).when(adSearchService).findById(invalidAdRequestId);

            mockMvc.perform(get("/api/v1/ads/{id}", invalidAdRequestId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.body.size()").value(1))
                    .andExpect(jsonPath("$.body[?(@.parameter=='id')]", notNullValue()));
        }
    }
}