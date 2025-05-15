package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static io.github.aglushkovsky.advertisingservice.util.AdServicesTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.SecurityTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdPromoteServiceTest {

    @Mock
    private AdCrudService adCrudService;

    @InjectMocks
    private AdPromoteService adPromoteService;

    @Test
    void promoteAdShouldChangeIsPromotedFlagWhenAdIdIsValidAndUserIsAuthorizedForPromoting() {
        Long adId = 1L;
        AdResponseDto targetAdStubResponseDto = createAdStubResponseDto(adId);
        AdResponseDto promotedTargetAdStubResponseDto = AdResponseDto.builder()
                .id(adId)
                .isPromoted(true)
                .build();
        AdCreateEditResponseDto editResponseDto = AdCreateEditResponseDto.builder()
                .isPromoted(true)
                .build();
        doReturn(targetAdStubResponseDto).when(adCrudService).findById(adId);
        setMockUserInSecurityContext(targetAdStubResponseDto.publisher().id());
        doReturn(promotedTargetAdStubResponseDto).when(adCrudService).editAd(adId, editResponseDto);

        AdResponseDto result = adPromoteService.promoteAd(adId);

        assertThat(result)
                .isNotNull()
                .isEqualTo(promotedTargetAdStubResponseDto);
        verify(adCrudService).findById(adId);
        verify(adCrudService).editAd(adId, editResponseDto);
    }

    @Test
    void promoteAdShouldThrowExceptionWhenAdIdIsNotValid() {
        Long adId = 1L;
        doThrow(NotFoundException.class).when(adCrudService).findById(adId);

        assertThatThrownBy(() -> adPromoteService.promoteAd(adId))
                .isInstanceOf(NotFoundException.class);
        verify(adCrudService).findById(adId);
    }

    @Test
    void promoteAdShouldThrowExceptionWhenUserIsNotAuthorizedForPromoting() {
        Long adId = 1L;
        AdResponseDto targetAdStubResponseDto = createAdStubResponseDto(adId);
        setMockUserInSecurityContext(10L);
        doReturn(targetAdStubResponseDto).when(adCrudService).findById(adId);

        assertThatThrownBy(() -> adPromoteService.promoteAd(adId))
                .isInstanceOf(AccessDeniedException.class);
        verify(adCrudService).findById(adId);
    }
}