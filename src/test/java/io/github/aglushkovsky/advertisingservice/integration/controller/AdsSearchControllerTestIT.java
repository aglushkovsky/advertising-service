package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

        @Test
        void searchAdsShouldReturnResultWhenParametersIsValid() throws Exception {
            String searchTerm = "macbook";
            Integer publisherId = 1;
            Integer localityId = 1;
            Integer page = 1;

            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("term", searchTerm)
                            .queryParam("onlyInTitle", "true")
                            .queryParam("minPrice", "10000000") // TODO подумать, оставить так или поменять?
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
        void searchAdsShouldReturnErrorResponseWhenFilterParametersIsInvalid() throws Exception {
            mockMvc.perform(get("/api/v1/ads")
                            .queryParam("minPrice", "-10000000") // TODO подумать, оставить так или поменять?
                            .queryParam("maxPrice", "-20000000")
                            .queryParam("localityId", "0")
                            .queryParam("publisherId", "0"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.body.size()").value(5))
                    .andExpect(jsonPath("$.body[*].parameter",
                            hasItems("minPrice", "maxPrice", "localityId", "publisherId", null)));
        }
    }
}
