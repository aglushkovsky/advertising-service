package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.service.AdCrudService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.github.aglushkovsky.advertisingservice.util.AdTestUtils.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcUnitTest(AdCrudController.class)
class AdCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdCrudService adCrudService;

    @Nested
    class CreateAd {

        @Test
        void createAdShouldCreateAdWhenAllParametersAreValid() throws Exception {
            AdCreateEditRequestDto adCreateEditRequestDto = AdCreateEditRequestDto.builder()
                    .title("Sample title")
                    .price(12345L)
                    .description("Sample description")
                    .localityId(1L)
                    .build();
            AdResponseDto expectedResponse = AdResponseDto.builder()
                    .id(1L)
                    .title(adCreateEditRequestDto.title())
                    .price(adCreateEditRequestDto.price())
                    .description(adCreateEditRequestDto.description())
                    .localityParts(List.of(
                            LocalityResponseDto.builder()
                                    .id(adCreateEditRequestDto.localityId())
                                    .build()))
                    .build();
            doReturn(expectedResponse).when(adCrudService).createAd(adCreateEditRequestDto);

            mockMvc.perform(post("/api/v1/ads")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adCreateEditRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        }

        @Test
        void createAdShouldReturnBadRequestResponseWhenAllParametersAreInvalid() throws Exception {
            AdCreateEditRequestDto adCreateEditRequestDto = AdCreateEditRequestDto.builder()
                    .title(null)
                    .price(null)
                    .description("test")
                    .localityId(0L)
                    .build();
            int countOfErrors = 4;

            mockMvc.perform(post("/api/v1/ads")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adCreateEditRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(countOfErrors));
        }
    }

    @Nested
    class EditAd {

        @Test
        void editAdShouldEditAdWhenAllParametersAreValid() throws Exception {
            Long targetAdId = 1L;
            AdCreateEditRequestDto adCreateEditRequestDto = AdCreateEditRequestDto.builder()
                    .title("Sample title")
                    .price(12345L)
                    .description("Sample description")
                    .localityId(1L)
                    .build();
            AdResponseDto expectedResponse = AdResponseDto.builder()
                    .id(targetAdId)
                    .title(adCreateEditRequestDto.title())
                    .price(adCreateEditRequestDto.price())
                    .description(adCreateEditRequestDto.description())
                    .localityParts(List.of(
                            LocalityResponseDto.builder()
                                    .id(adCreateEditRequestDto.localityId())
                                    .build()))
                    .build();
            doReturn(expectedResponse).when(adCrudService).editAd(targetAdId, adCreateEditRequestDto);

            mockMvc.perform(patch("/api/v1/ads/{0}", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adCreateEditRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        }

        @Test
        void editAdShouldReturnBadRequestResponseWhenAllParametersAreInvalid() throws Exception {
            Long targetAdId = 0L;
            AdCreateEditRequestDto adCreateEditRequestDto = AdCreateEditRequestDto.builder()
                    .title("test")
                    .price(-10L)
                    .description("test")
                    .localityId(0L)
                    .build();
            int countOfErrors = 5;

            mockMvc.perform(patch("/api/v1/ads/{0}", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adCreateEditRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(countOfErrors));
        }

        @Test
        void editAdShouldReturnNotFoundResponseWhenTargetAdIdDoesNotExists() throws Exception {
            Long targetAdId = 1L;
            AdCreateEditRequestDto adCreateEditRequestDto = AdCreateEditRequestDto.builder().build();
            doThrow(NotFoundException.class).when(adCrudService).editAd(targetAdId, adCreateEditRequestDto);

            mockMvc.perform(patch("/api/v1/ads/{0}", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adCreateEditRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()));
        }

        @Test
        void editAdShouldReturnForbiddenResponseWhenUserIsNotAuthorizedForThisAction() throws Exception {
            Long targetAdId = 1L;
            AdCreateEditRequestDto adCreateEditRequestDto = AdCreateEditRequestDto.builder().build();
            doThrow(AccessDeniedException.class).when(adCrudService).editAd(targetAdId, adCreateEditRequestDto);

            mockMvc.perform(patch("/api/v1/ads/{0}", targetAdId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adCreateEditRequestDto)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value(FORBIDDEN.value()));
        }
    }

    @Nested
    class DeleteAd {

        @Test
        void deleteAdShouldEditAdWhenAllParametersAreValid() throws Exception {
            Long targetAdId = 1L;

            mockMvc.perform(delete("/api/v1/ads/{0}", targetAdId))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteAdShouldReturnBadRequestResponseWhenAllParametersAreInvalid() throws Exception {
            Long targetAdId = 0L;

            mockMvc.perform(delete("/api/v1/ads/{0}", targetAdId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }

        @Test
        void deleteAdShouldReturnNotFoundResponseWhenTargetAdIdDoesNotExists() throws Exception {
            Long targetAdId = 1L;
            doThrow(NotFoundException.class).when(adCrudService).deleteAd(targetAdId);

            mockMvc.perform(delete("/api/v1/ads/{0}", targetAdId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()));
        }

        @Test
        void editAdShouldReturnForbiddenResponseWhenUserIsNotAuthorizedForThisAction() throws Exception {
            Long targetAdId = 1L;
            doThrow(AccessDeniedException.class).when(adCrudService).deleteAd(targetAdId);

            mockMvc.perform(delete("/api/v1/ads/{0}", targetAdId))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value(FORBIDDEN.value()));
        }
    }

    @Nested
    class FindById {

        @Test
        void findByIdShouldReturnResultWhenAllParametersAreValid() throws Exception {
            Long adRequestId = 1L;
            AdResponseDto adStubResponseDto = createAdStubResponseDto(adRequestId);
            doReturn(adStubResponseDto).when(adCrudService).findById(adRequestId);

            mockMvc.perform(get("/api/v1/ads/{0}", adRequestId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(adStubResponseDto)));
        }

        @Test
        void findByIdShouldReturnNotFoundResponseWhenItDoesNotExist() throws Exception {
            Long adRequestId = 1L;
            doThrow(NotFoundException.class).when(adCrudService).findById(adRequestId);

            mockMvc.perform(get("/api/v1/ads/{id}", adRequestId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()));
        }

        @Test
        void findByIdShouldReturnBadRequestResponseWhenIdIsInvalid() throws Exception {
            Long invalidAdRequestId = 0L;
            doThrow(NotFoundException.class).when(adCrudService).findById(invalidAdRequestId);

            mockMvc.perform(get("/api/v1/ads/{id}", invalidAdRequestId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }
    }
}