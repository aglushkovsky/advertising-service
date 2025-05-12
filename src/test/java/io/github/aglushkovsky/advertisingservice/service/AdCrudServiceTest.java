package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapperImpl;
import io.github.aglushkovsky.advertisingservice.mapper.LocalityMapperImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType.CITY;
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.Role.USER;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringJUnitConfig({AdCrudService.class, AdMapperImpl.class, LocalityMapperImpl.class})
class AdCrudServiceTest {

    @MockitoBean
    private AdDao adDao;

    @Autowired
    private AdCrudService adCrudService;

    @Nested
    class FindById {

        @Test
        void findByIdShouldReturnResultWhenAdIdExists() {
            // TODO Надо вынести куда-то в одно место создание тестовых объектов сущностей.
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
            LocalDateTime createdAt = LocalDateTime.parse("2025-05-12T13:51:48.899862700");
            Long adId = 1L;
            Ad adStub = new Ad(
                    adId,
                    "ad title",
                    new BigDecimal("12345"),
                    null,
                    new Locality(1L, "Test City", emptyList(), emptyList(), CITY),
                    user,
                    createdAt,
                    false
            );
            doReturn(Optional.of(adStub)).when(adDao).findById(adId);

            AdResponseDto adResponseDto = adCrudService.findById(adId);

            assertThat(adResponseDto.id()).isEqualTo(adId);
        }

        @Test
        void findByIdShouldReturnResultWhenAdIdDoesNotExists() {
            Long adId = 1L;
            doReturn(Optional.empty()).when(adDao).findById(adId);

            assertThatThrownBy(() -> adCrudService.findById(adId)).isInstanceOf(NotFoundException.class);
        }
    }
}