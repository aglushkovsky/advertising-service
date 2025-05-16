package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AdPromoteService;
import io.github.aglushkovsky.advertisingservice.util.AdTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcUnitTest(AdPromoteController.class)
class AdPromoteControllerTest {

    @MockitoBean
    private AdPromoteService adPromoteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void promoteAdShouldPromoteAdWhenTargetAdIsValid() throws Exception {
        Long targetAdId = 1L;
        AdResponseDto adStubResponseDto = AdTestUtils.createAdStubResponseDto(targetAdId);
        doReturn(adStubResponseDto).when(adPromoteService).promoteAd(targetAdId);

        mockMvc.perform(patch("/api/v1/ads/{0}/promote", targetAdId))
                .andExpect(status().isOk());
    }

    @Test
    void promoteAdShouldReturnBadRequestWhenTargetAdIsInvalid() throws Exception {
        Long invalidTargetAdId = 0L;

        mockMvc.perform(patch("/api/v1/ads/{0}/promote", invalidTargetAdId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.size()").value(1));
    }
}