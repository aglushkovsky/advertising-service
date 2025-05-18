package io.github.aglushkovsky.advertisingservice.integration.controller;

import com.jayway.jsonpath.JsonPath;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO дописать тесты
public class AdCrudControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdDao adDao;

    @Nested
    class CreateAd {

        @Test
        void createAdShouldReturnStatusCreated() throws Exception {
            String createAdRequest = """
                    {
                      "title": "Продам гараж",
                      "price": "45000000",
                      "description": "Гараж в хорошем состоянии.\\nТакже в гараже есть яма и погреб",
                      "localityId": 2
                    }
                    """;

            MvcResult result = mockMvc.perform(post("/api/v1/ads")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createAdRequest))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("Продам гараж"))
                    .andExpect(jsonPath("$.price").value("45000000"))
                    .andExpect(jsonPath("$.description")
                            .value("Гараж в хорошем состоянии.\nТакже в гараже есть яма и погреб"))
                    .andExpect(jsonPath("$.localityParts[*].id", hasItem(2)))
                    .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            Integer id = JsonPath.parse(contentAsString).read("$.id");

            Ad ad = adDao.findById(id.longValue()).orElseThrow();
            assertThat(ad.getTitle()).isEqualTo("Продам гараж");
            assertThat(ad.getPrice()).isEqualTo(45000000);
            assertThat(ad.getDescription())
                    .isEqualTo("Гараж в хорошем состоянии.\nТакже в гараже есть яма и погреб");
            assertThat(ad.getLocality().getId()).isEqualTo(2L);
        }

        @Test
        void createAdShouldReturnNotFoundStatus() throws Exception {
            String createAdRequest = """
                    {
                      "title": "Продам гараж",
                      "price": "45000000",
                      "description": "Гараж в хорошем состоянии.\\nТакже в гараже есть яма и погреб",
                      "localityId": 1000
                    }
                    """;

            mockMvc.perform(post("/api/v1/ads")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createAdRequest))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }
    }

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
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }
    }
}
