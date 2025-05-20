package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.LocalityMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalityServiceTest {

    @Mock
    private LocalityDao localityDao;

    @Mock
    private LocalityMapper localityMapper;

    @InjectMocks
    private LocalityService localityService;

    @Test
    void findAllByLocalityTypeShouldReturnMappedDtoListWhenTypeIsValid() {
        LocalityType cityType = LocalityType.CITY;
        Locality mockLocality = mock(Locality.class);
        doReturn(List.of(mockLocality)).when(localityDao).findAllByLocalityType(cityType);
        LocalityResponseDto resultListItem = mock(LocalityResponseDto.class);
        doReturn(resultListItem).when(localityMapper).toDto(mockLocality);

        List<LocalityResponseDto> result = localityService.findAllByLocalityType(cityType);

        assertThat(result)
                .isNotNull()
                .isEqualTo(List.of(resultListItem));
        verify(localityDao).findAllByLocalityType(cityType);
        verify(localityMapper).toDto(mockLocality);
    }

    @Nested
    class FindDirectDescendantsByLocalityId {

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnMappedDtoListWhenLocalityIdExists() {
            Long localityId = 1L;
            doReturn(true).when(localityDao).isExists(localityId);
            Locality mockLocality = mock(Locality.class);
            doReturn(List.of(mockLocality)).when(localityDao).findDirectDescendantsByLocalityId(localityId);
            LocalityResponseDto localityResponseDtoMock = mock(LocalityResponseDto.class);
            doReturn(localityResponseDtoMock).when(localityMapper).toDto(mockLocality);

            List<LocalityResponseDto> result = localityService.findDirectDescendantsByLocalityId(localityId);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(List.of(localityResponseDtoMock));
            verify(localityDao).isExists(localityId);
            verify(localityDao).findDirectDescendantsByLocalityId(localityId);
            verify(localityMapper).toDto(mockLocality);
        }

        @Test
        void findDirectDescendantsByLocalityIdShouldThrowExceptionWhenLocalityIdNotExists() {
            Long localityId = 1L;
            doReturn(false).when(localityDao).isExists(localityId);

            assertThatThrownBy(() -> localityService.findDirectDescendantsByLocalityId(localityId))
                    .isInstanceOf(NotFoundException.class);
            verify(localityDao).isExists(localityId);
        }
    }
}