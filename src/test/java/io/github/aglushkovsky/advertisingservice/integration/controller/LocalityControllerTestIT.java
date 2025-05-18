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

    @Nested
    class FindAllByLocalityType {

        @Test
        void findAllByLocalityTypeShouldReturnResult() throws Exception {
            mockMvc.perform(get("/api/v1/localities"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(3))
                    .andExpect(jsonPath("$.[*].name", hasItems("Орёл", "Мценск", "Брянск")))
                    .andExpect(jsonPath("$.[*].type", everyItem(is(LocalityType.CITY.name()))));
        }

        @Test
        void findAllByLocalityTypeShouldReturnErrorResponseWhenLocalityTypeIsInvalid() throws Exception {
            String invalidLocalityType = "invalid";

            mockMvc.perform(get("/api/v1/localities?type={0}", invalidLocalityType))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }
    }

    @Nested
    class FindDirectDescendantsByLocalityId {

        @Test
        void findDirectDescendantsByLocalityId() throws Exception {
            Long localityId = 1L;

            mockMvc.perform(get("/api/v1/localities/{0}/descendants", localityId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(4))
                    .andExpect(jsonPath("$.[*].name",
                            hasItems("Советский", "Железнодорожный", "Заводской", "Северный")))
                    .andExpect(jsonPath("$.[*].type", everyItem(is(LocalityType.DISTRICT.name()))));
        }

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnErrorResponseWhenLocalityIdIsInvalid() throws Exception {
            Long invalidLocalityId = 0L;

            mockMvc.perform(get("/api/v1/localities/{0}/descendants", invalidLocalityId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }
    }
}
