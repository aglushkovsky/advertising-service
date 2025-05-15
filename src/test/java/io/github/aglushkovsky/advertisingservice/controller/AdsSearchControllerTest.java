package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.service.AdsSearchService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.aglushkovsky.advertisingservice.util.AdTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.PageableTestCommonUtils.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcUnitTest(AdsSearchController.class)
class AdsSearchControllerTest {

    @MockitoBean
    private AdsSearchService adsSearchService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SearchAds {

        @Test
        void searchAdsShouldReturnFirstPageFromAllLocalitiesWhenFilterAndPageableNotSpecified() throws Exception {
            PageEntity<AdResponseDto> findAllResultStub = createAdResponseDtoStubPageEntityStub();
            doReturn(findAllResultStub).when(adsSearchService).findAll(any(), any());

            mockMvc.perform(get("/api/v1/ads"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(findAllResultStub)));
        }

        @Test
        void searchAdsShouldReturnFirstPageFromAllLocalitiesWhenFilterAndPageableParametersWasSpecified() throws Exception {
            PageEntity<AdResponseDto> findAllResultStub = createAdResponseDtoStubPageEntityStub();
            doReturn(findAllResultStub).when(adsSearchService).findAll(any(), any());

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
    class GetAdsHistoryByUserId {

        @Test
        void getAdsHistoryByUserIdShouldReturnResultWhenAllParametersAreValid() throws Exception {
            Long userId = 1L;
            PageEntity<AdResponseDto> pageEntityStubWithSingleRecord = createPageEntityStubWithSingleRecord(mock(AdResponseDto.class));
            PageableRequestDto pageable = createPageableRequestDto();
            doReturn(pageEntityStubWithSingleRecord).when(adsSearchService).getAdsHistoryByUserId(userId, pageable);

            mockMvc.perform(get("/api/v1/users/{0}/ads/history", userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.body.size()").value(1))
                    .andExpect(jsonPath("$.metadata.currentPage").value(pageable.page()));
        }

        @Test
        void getAdsHistoryByUserIdShouldReturnBadRequestResponseWhenUserIdIsInvalid() throws Exception {
            Long userId = 0L;

            mockMvc.perform(get("/api/v1/users/{0}/ads/history", userId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()));
        }

        @Test
        void getAdsHistoryByUserIdShouldReturnBadRequestResponseWhenPageableParametersAreInvalid() throws Exception {
            Long userId = 1L;

            mockMvc.perform(get("/api/v1/users/{0}/ads/history", userId)
                            .queryParam("limit", "0")
                            .queryParam("page", "0"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.body.size()").value(1))
                    .andExpect(jsonPath("$.body[?(@.parameter=='pageable')].messages.size()")
                            .value(2));
        }

        @Test
        void getAdsHistoryByUserIdShouldReturnNotFoundResponseWhenUserIdIsNotExists() throws Exception {
            Long userId = 12345L;
            PageableRequestDto pageable = createPageableRequestDto();
            doThrow(NotFoundException.class).when(adsSearchService).getAdsHistoryByUserId(userId, pageable);

            mockMvc.perform(get("/api/v1/users/{0}/ads/history", userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()));
        }
    }
}