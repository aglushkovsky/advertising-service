package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.service.LocalityService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcUnitTest(LocalityController.class)
class LocalityControllerTest {

    @MockitoBean
    private LocalityService localityService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class FindDirectDescendantsByLocalityId {

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnResultWhenIdIsValid() throws Exception {
            Long localityId = 1L;
            doReturn(List.of(mock(LocalityResponseDto.class)))
                    .when(localityService).findDirectDescendantsByLocalityId(localityId);

            mockMvc.perform(get("/api/v1/localities/{id}/descendants", localityId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnBadRequestResponseWhenIdIsInvalid() throws Exception {
            Long invalidLocalityId = 0L;

            mockMvc.perform(get("/api/v1/localities/{id}/descendants", invalidLocalityId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()));
        }
    }

    @Nested
    class FindAllByLocalityType {

        @Test
        void findAllByLocalityTypeShouldReturnResultWhenLocalityTypeValueIsValid() throws Exception {
            LocalityType localityType = LocalityType.CITY;
            doReturn(List.of(mock(LocalityResponseDto.class))).when(localityService).findAllByLocalityType(localityType);

            mockMvc.perform(get("/api/v1/localities")
                            .queryParam("type", localityType.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void findAllByLocalityTypeShouldReturnBadRequestResponseWhenLocalityTypeValueIsInvalid() throws Exception {
            String invalidLocalityType = "INVALID_TYPE";

            mockMvc.perform(get("/api/v1/localities")
                            .queryParam("type", invalidLocalityType))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()));
        }
    }
}