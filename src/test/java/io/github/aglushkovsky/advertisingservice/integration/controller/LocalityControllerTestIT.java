package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LocalityControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllByLocalityTypeShouldReturnResult() throws Exception {
        mockMvc.perform(get("/api/v1/localities")
                        .queryParam("type", LocalityType.CITY.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$.[*].name", hasItems("Орёл", "Мценск", "Брянск")))
                .andExpect(jsonPath("$.[*].type", everyItem(is(LocalityType.CITY.name()))));
    }

    @Nested
    class FindDirectDescendantsByLocalityId {

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnResultWhenLocalityIdIsExists() throws Exception {
            Long localityId = 1L;

            mockMvc.perform(get("/api/v1/localities/{0}/descendants", localityId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(4))
                    .andExpect(jsonPath("$.[*].name",
                            hasItems("Советский", "Железнодорожный", "Заводской", "Северный")))
                    .andExpect(jsonPath("$.[*].type", everyItem(is(LocalityType.DISTRICT.name()))));
        }

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnNotFoundResponseWhenLocalityIdDoesNotExists() throws Exception {
            Long localityId = 1000L;

            mockMvc.perform(get("/api/v1/localities/{0}/descendants", localityId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }
    }
}
