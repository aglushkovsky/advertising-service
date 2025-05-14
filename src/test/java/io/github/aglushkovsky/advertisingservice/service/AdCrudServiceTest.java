package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.enumeration.AdStatus.*;
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType.CITY;
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.Role.USER;
import static java.util.Collections.*;
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
            User user = new User(
                    1L,
                    "test_user",
                    "password_hash",
                    null,
                    null,
                    USER,
                    0.0,
                    emptyList(),
                    emptyList(),
                    emptyList()
            );
            Locality testCity = new Locality(1L, "Test City", emptyList(), emptyList(), CITY);
            Ad adStub = new Ad(
                    adId,
                    "ad title",
                    12345L,
                    null,
                    testCity,
                    user,
                    LocalDateTime.parse("2025-05-12T13:51:48.899862700"),
                    ACTIVE,
                    false
            );
            AdResponseDto adStubResponseDto = new AdResponseDto(
                    adId,
                    adStub.getTitle(),
                    adStub.getPrice(),
                    adStub.getDescription(),
                    List.of(
                            new LocalityResponseDto(
                                    adStub.getLocality().getId(),
                                    adStub.getLocality().getName(),
                                    adStub.getLocality().getType().name()
                            )
                    ),
                    new UserResponseDto(
                            user.getId(),
                            user.getLogin(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getTotalRating()
                    ),
                    adStub.getPublishedAt().toString(),
                    adStub.getStatus().name(),
                    adStub.getIsPromoted()
            );
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
}