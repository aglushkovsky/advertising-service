package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.service.LocalityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcUnitTest(LocalityController.class)
class LocalityControllerTest {

    @MockitoBean
    private LocalityService localityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findDirectDescendantsByLocalityIdShouldReturnResultWhenIdIsValid() throws Exception {
        Long localityId = 1L;
        doReturn(emptyList()).when(localityService).findDirectDescendantsByLocalityId(localityId);

        mockMvc.perform(get("/api/v1/localities/{id}/descendants", localityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void findDirectDescendantsByLocalityIdShouldReturnResultWhenIdIsInvalid() throws Exception {
        Long invalidLocalityId = 0L;

        mockMvc.perform(get("/api/v1/localities/{id}/descendants", invalidLocalityId))
                .andExpect(status().isBadRequest());
    }
}