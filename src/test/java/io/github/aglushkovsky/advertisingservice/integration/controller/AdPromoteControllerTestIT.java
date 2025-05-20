package io.github.aglushkovsky.advertisingservice.integration.controller;

import io.github.aglushkovsky.advertisingservice.annotation.WithJwtAuthenticationContext;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdPromoteControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdDao adDao;

    @Test
    void adPromoteShouldChangeIsPromotedFlagWhenAdIdIsExistsAndUserIsAuthorizedForThisAction() throws Exception {
        Long targetAdId = 2L;

        mockMvc.perform(patch("/api/v1/ads/{0}/promote", targetAdId))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.isPromoted").value(true));

        Ad ad = adDao.findById(targetAdId).orElseThrow();
        assertThat(ad.getIsPromoted()).isTrue();
    }

    @Test
    @WithJwtAuthenticationContext(id = 10L)
    void adPromoteShouldReturnForbiddenResponseWhenAdIdIsExistsButUserIsNotAuthorizedForThisAction() throws Exception {
        Long targetAdId = 2L;

        mockMvc.perform(patch("/api/v1/ads/{0}/promote", targetAdId))
                .andExpect(status().isForbidden());

        Ad ad = adDao.findById(targetAdId).orElseThrow();
        assertThat(ad.getIsPromoted()).isFalse();
    }

    @Test
    void adPromoteShouldReturnNotFoundExceptionWhenAdIdDoesNotExists() throws Exception {
        Long targetAdId = 10000L;

        mockMvc.perform(patch("/api/v1/ads/{0}/promote", targetAdId))
                .andExpect(status().isNotFound());
    }
}
