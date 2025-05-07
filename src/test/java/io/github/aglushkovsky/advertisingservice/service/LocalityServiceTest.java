package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.LocalityMapperImpl;
import io.github.aglushkovsky.advertisingservice.test.config.MapperConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {LocalityService.class, MapperConfig.class})
class LocalityServiceTest {

    @MockitoBean
    private LocalityDao localityDao;

    @Autowired
    private LocalityService localityService;

    @Test
    void findAllByLocalityTypeShouldReturnMappedDtoListWhenTypeIsValid() {
        LocalityType cityType = LocalityType.CITY;
        List<Locality> mockLocalities = List.of(
                new Locality(null, null, List.of(), List.of(), cityType),
                new Locality(null, null, List.of(), List.of(), cityType));
        doReturn(mockLocalities).when(localityDao).findAllByLocalityType(cityType);

        List<LocalityResponseDto> result = localityService.findAllByLocalityType(cityType);

        assertThat(result)
                .hasSize(mockLocalities.size())
                .allSatisfy(localityDto -> assertThat(localityDto.type()).isEqualTo(cityType.name()));
    }

    @Nested
    class FindDirectDescendantsByLocalityId {

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnMappedDtoListWhenLocalityIdExists() {
            Long localityId = 1L;
            doReturn(true).when(localityDao).isExists(localityId);
            List<Locality> mockLocalities = List.of(mock(Locality.class), mock(Locality.class));
            doReturn(mockLocalities).when(localityDao).findDirectDescendantsByLocalityId(localityId);

            List<LocalityResponseDto> result = localityService.findDirectDescendantsByLocalityId(localityId);

            assertThat(result).hasSize(mockLocalities.size());
        }

        @Test
        void findDirectDescendantsByLocalityIdShouldReturnMappedDtoListWhenLocalityIdNotExists() {
            Long localityId = 1L;
            doReturn(false).when(localityDao).isExists(localityId);

            assertThatThrownBy(() -> localityService.findDirectDescendantsByLocalityId(localityId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}