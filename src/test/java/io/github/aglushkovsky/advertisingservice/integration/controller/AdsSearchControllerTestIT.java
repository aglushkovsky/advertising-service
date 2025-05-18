package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdsSearchControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class SearchAds {

        @ParameterizedTest
        @ValueSource(strings = {"Macbook", "macbook", "Macbook pro", "Macbook Pro", "macbook Pro", "macbook pro"})
        void searchAdsShouldReturnResultWhenParametersIsValid(String searchTerm) throws Exception {
            Integer publisherId = 1;
            Integer localityId = 1;
            Integer page = 1;

            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("term", searchTerm)
                            .queryParam("onlyInTitle", "1")
                            .queryParam("minPrice", "10000000")
                            .queryParam("maxPrice", "20000000")
                            .queryParam("localityId", localityId.toString())
                            .queryParam("publisherId", publisherId.toString())
                            .queryParam("page", page.toString())
                            .queryParam("limit", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.body.size()").value(1))
                    .andExpect(jsonPath("$.body[0].title", containsStringIgnoringCase(searchTerm)))
                    .andExpect(jsonPath("$.body[0].publisher.id", is(publisherId)))
                    .andExpect(jsonPath("$.body[0].localityParts[*].id", hasItem(localityId)))
                    .andExpect(jsonPath("$.metadata.currentPage").value(1))
                    .andExpect(jsonPath("$.metadata.totalPages").value(page))
                    .andExpect(jsonPath("$.metadata.totalRecords").value(1))
                    .andExpect(jsonPath("$.metadata.isLastPage").value(true));
        }

        @Test
        void searchAdsShouldReturnNotFoundResponseWhenPublisherIdDoesNotExists() throws Exception {
            long nonExistentPublisherId = 1000L;

            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("publisherId", Long.toString(nonExistentPublisherId)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        void searchAdsShouldReturnNotFoundResponseWhenLocalityIdDoesNotExists() throws Exception {
            long nonExistentLocalityId = 1000L;

            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("localityId", Long.toString(nonExistentLocalityId)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }
    }

    @Nested
    class GetAdsHistoryByUserId {

        @Test
        void getAdsHistoryByUserIdShouldReturnResultWhenParametersIsValid() throws Exception {
            Long userId = 1L;

            mockMvc.perform(get("/api/v1/users/{0}/ads/history", userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.body.size()").value(1))
                    .andExpect(jsonPath("$.body[0].publisher.id", is(1)))
                    .andExpect(jsonPath("$.body[0].status", is("SOLD")));
        }

        @Test
        void getAdsHistoryByUserIdShouldReturnNotFoundResponseWhenPublisherIdDoesNotExists() throws Exception {
            long nonExistentPublisherId = 1000L;

            mockMvc.perform(get("/api/v1/users/{0}/ads/history", nonExistentPublisherId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }
    }
}
