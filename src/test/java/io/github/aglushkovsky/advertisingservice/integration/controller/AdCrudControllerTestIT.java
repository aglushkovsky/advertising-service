package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdCrudControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class FindById {

        @Test
        void findByIdShouldReturnResultWhenIdIsValid() throws Exception {
            Long adId = 1L;

            mockMvc.perform(get("/api/v1/ads/{0}", adId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(adId));
        }

        @Test
        void findByIdShouldReturnResultWhenIdDoesNotExists() throws Exception {
            Long adId = 1000L;

            mockMvc.perform(get("/api/v1/ads/{0}", adId))
                    .andExpect(status().isNotFound());
        }

        @Test
        void findByIdShouldReturnResultWhenIdIsInvalid() throws Exception {
            Long invalidId = -1L;

            mockMvc.perform(get("/api/v1/ads/{0}", invalidId))
                    .andExpect(status().isBadRequest());
        }
    }
}
