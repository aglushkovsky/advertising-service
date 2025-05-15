package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.enumeration.AdStatus.*;
import static io.github.aglushkovsky.advertisingservice.util.AdTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.SecurityTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AdCrudServiceTest {

    @Mock
    private AdDao adDao;

    @Mock
    private AdMapper adMapper;

    @InjectMocks
    private AdCrudService adCrudService;

    @Nested
    class FindById {

        @Test
        void findByIdShouldReturnResultWhenAdIdExists() {
            Long adId = 1L;
            Ad adStub = createAdStub(adId);
            AdResponseDto adStubResponseDto = createAdStubResponseDto(adId);
            doReturn(Optional.of(adStub)).when(adDao).findById(adId);
            doReturn(adStubResponseDto).when(adMapper).toDto(adStub);

            AdResponseDto result = adCrudService.findById(adId);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(adStubResponseDto);
            verify(adDao).findById(adId);
            verify(adMapper).toDto(adStub);
        }

        @Test
        void findByIdShouldReturnResultWhenAdIdDoesNotExists() {
            Long adId = 1L;
            doReturn(Optional.empty()).when(adDao).findById(adId);

            assertThatThrownBy(() -> adCrudService.findById(adId))
                    .isInstanceOf(NotFoundException.class);
            verify(adDao).findById(adId);
        }
    }

    @Nested
    class CreateAd {

        @Test
        void createAdShouldCreateNewAd() {
            AdCreateEditRequestDto adCreateEditRequestDto =
                    AdCreateEditRequestDto.builder()
                            .title("Title")
                            .price(12345L)
                            .description(null)
                            .localityId(1L)
                            .isPromoted(false)
                            .status(ACTIVE)
                            .build();
            Ad adStubWithoutId = createAdStub(null);
            Ad adStubWithId = createAdStub(1L);
            AdResponseDto adStubResponseDto = createAdStubResponseDto(adStubWithId.getId());
            doReturn(adStubWithoutId).when(adMapper).toEntity(adCreateEditRequestDto);
            doReturn(adStubWithId).when(adDao).add(adStubWithoutId);
            doReturn(adStubResponseDto).when(adMapper).toDto(adStubWithId);

            AdResponseDto result = adCrudService.createAd(adCreateEditRequestDto);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(adStubResponseDto);
            verify(adMapper).toEntity(adCreateEditRequestDto);
            verify(adDao).add(adStubWithoutId);
            verify(adMapper).toDto(adStubWithId);
        }
    }

    @Nested
    class EditAd {

        private static final AdCreateEditRequestDto AD_CREATE_EDIT_REQUEST_DTO =
                AdCreateEditRequestDto.builder()
                        .title("Title")
                        .price(12345L)
                        .description(null)
                        .localityId(1L)
                        .isPromoted(false)
                        .status(ACTIVE)
                        .build();

        @Test
        void editAdShouldEditAdWhenIdExistsAndUserIsAuthorizedForEdit() {
            Long adId = 1L;
            Ad adStub = createAdStub(adId);
            Ad updatedAdStub = createAdStub(adId);
            AdResponseDto updatedAdResponseDtoStub = createAdStubResponseDto(adId);
            doReturn(Optional.of(adStub)).when(adDao).findById(adId);
            setMockUserInSecurityContext(1L);
            doReturn(updatedAdStub).when(adMapper).editAd(adStub, AD_CREATE_EDIT_REQUEST_DTO);
            doReturn(updatedAdResponseDtoStub).when(adMapper).toDto(updatedAdStub);

            AdResponseDto result = adCrudService.editAd(adId, AD_CREATE_EDIT_REQUEST_DTO);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(updatedAdResponseDtoStub);
            verify(adDao).findById(adId);
            verify(adMapper).editAd(adStub, AD_CREATE_EDIT_REQUEST_DTO);
            verify(adMapper).toDto(updatedAdStub);
        }

        @Test
        void editAdShouldThrowExceptionWhenAdIdDoesNotExists() {
            Long adId = 1L;
            doReturn(Optional.empty()).when(adDao).findById(adId);

            assertThatThrownBy(() -> adCrudService.editAd(adId, AD_CREATE_EDIT_REQUEST_DTO))
                    .isInstanceOf(NotFoundException.class);
            verify(adDao).findById(adId);
        }

        @Test
        void editAdShouldThrowExceptionWhenUserIsNotAuthorizedForEdit() {
            Long adId = 1L;
            Ad adStub = createAdStub(adId);
            doReturn(Optional.of(adStub)).when(adDao).findById(adId);
            setMockUserInSecurityContext(10L);

            assertThatThrownBy(() -> adCrudService.editAd(adId, AD_CREATE_EDIT_REQUEST_DTO))
                    .isInstanceOf(AccessDeniedException.class);
            verify(adDao).findById(adId);
        }
    }

    @Nested
    class DeleteAd {

        @Test
        void editAdShouldEditAdWhenIdExistsAndUserIsAuthorizedForDelete() {
            Long adId = 1L;
            Ad adForDeleteStub = createAdStub(adId);
            doReturn(Optional.of(adForDeleteStub)).when(adDao).findById(adId);
            setMockUserInSecurityContext(1L);

            adCrudService.deleteAd(adId);

            verify(adDao).findById(adId);
            verify(adDao).delete(adForDeleteStub);
        }

        @Test
        void editAdShouldThrowExceptionWhenAdIdDoesNotExists() {
            Long adId = 1L;
            doReturn(Optional.empty()).when(adDao).findById(adId);

            assertThatThrownBy(() -> adCrudService.deleteAd(adId))
                    .isInstanceOf(NotFoundException.class);
            verify(adDao).findById(adId);
        }

        @Test
        void editAdShouldThrowExceptionWhenUserIsNotAuthorizedForDelete() {
            Long adId = 1L;
            Ad adStub = createAdStub(adId);
            doReturn(Optional.of(adStub)).when(adDao).findById(adId);
            setMockUserInSecurityContext(10L);

            assertThatThrownBy(() -> adCrudService.deleteAd(adId))
                    .isInstanceOf(AccessDeniedException.class);
            verify(adDao).findById(adId);
        }
    }
}