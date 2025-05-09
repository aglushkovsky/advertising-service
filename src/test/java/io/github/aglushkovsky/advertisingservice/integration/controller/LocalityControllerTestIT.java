package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LocalityControllerTestIT extends AbstractIntegrationTest { // TODO Посмотреть про зарезервированные постфиксы IT в Maven

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllByLocalityTypeShouldReturnResult() throws Exception {
        mockMvc.perform(get("/api/v1/localities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[?(@.id==1)].name").value("Орёл"))
                .andExpect(jsonPath("$[?(@.id==8)].name").value("Мценск"))
                .andExpect(jsonPath("$[?(@.id==18)].name").value("Брянск"))
                .andExpect(jsonPath("$.[*].type", everyItem(is(LocalityType.CITY.name()))));
    }

    @Test
    void findAllByLocalityTypeShouldReturnErrorResponseWhenLocalityTypeIsInvalid() throws Exception {
        String invalidLocalityType = "invalid";
        mockMvc.perform(get("/api/v1/localities?type={0}", invalidLocalityType))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.body[?(@.parameter=='type')]").exists());
    }

    @Test
    void findDirectDescendantsByLocalityId() throws Exception {
        Long localityId = 1L;
        mockMvc.perform(get("/api/v1/localities/{0}/descendants", localityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$[?(@.id==2)].name").value("Советский"))
                .andExpect(jsonPath("$[?(@.id==3)].name").value("Железнодорожный"))
                .andExpect(jsonPath("$[?(@.id==4)].name").value("Заводской"))
                .andExpect(jsonPath("$[?(@.id==5)].name").value("Северный"))
                .andExpect(jsonPath("$.[*].type", everyItem(is(LocalityType.DISTRICT.name()))));
    }

    @Test
    void findDirectDescendantsByLocalityIdShouldReturnErrorResponseWhenLocalityIdIsInvalid() throws Exception {
        Long invalidLocalityId = 0L;
        mockMvc.perform(get("/api/v1/localities/{0}/descendants", invalidLocalityId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.body[?(@.parameter=='id')]").exists());
    }
}
