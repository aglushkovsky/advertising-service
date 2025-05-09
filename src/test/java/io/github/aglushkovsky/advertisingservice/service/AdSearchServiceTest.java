package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.test.config.MapperConfig;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {AdSearchService.class, MapperConfig.class, ValidationAutoConfiguration.class})
class AdSearchServiceTest {

    private static final Long DEFAULT_LIMIT = 50L;

    private static final Long DEFAULT_PAGE = 1L;

    private static final PageEntity<Ad> EMPTY_AD_PAGE = new PageEntity<>(
            emptyList(),
            new PageEntity.Metadata(1L, 1L, 0L, true)
    );

    @Autowired
    private AdSearchService adSearchService;

    @MockitoBean
    private AdDao adDao;

    @MockitoBean
    private LocalityDao localityDao;

    @MockitoBean
    private UserDao userDao;

    @Nested
    class SearchByTerm {

        @ParameterizedTest
        @ValueSource(strings = {"macbook", "Macbook", "macbook pro", "Macbook pro", "Macbook Pro", "macbook Pro", "mAcbook"})
        void findAllShouldReturnAdsListWhenTermMatchWasFoundInTitleWithIgnoringCase(String term) {
            Ad ad = new Ad(1L,
                    "Macbook Pro M4 16/512 новый запечатанный из ОАЭ",
                    new BigDecimal("15000000"),
                    null,
                    new Locality(1L, "Орёл", List.of(), List.of(), LocalityType.CITY),
                    new User(),
                    LocalDateTime.now(),
                    true);
            var filter = new FindAllAdsFilterRequestDto(
                    term,
                    true,
                    null,
                    null,
                    null,
                    null);
            var pageable = new PageableRequestDto(DEFAULT_LIMIT, DEFAULT_PAGE);
            PageEntity<Ad> pageEntityStub = new PageEntity<>(
                    List.of(ad),
                    new PageEntity.Metadata(1L, 1L, 1L, true)
            );
            doReturn(pageEntityStub).when(adDao).findAll(anyLong(), anyLong(), any(), any());

            PageEntity<AdResponseDto> ads = adSearchService.findAll(filter, pageable);

            assertThat(ads.body()).hasSize(1);
            assertThat(ads.body().stream().findFirst().map(AdResponseDto::title).orElseThrow()).containsIgnoringCase(term);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void findAllShouldReturnEmptyListWhenThereAreNoMatchesForTermInTitleAndDescription(Boolean onlyInTitle) {
            doReturn(EMPTY_AD_PAGE).when(adDao).findAll(anyLong(), anyLong(), any(), any());
            FindAllAdsFilterRequestDto filter = new FindAllAdsFilterRequestDto(
                    "dasddsad",
                    onlyInTitle,
                    null,
                    null,
                    null,
                    null);
            var pageable = new PageableRequestDto(DEFAULT_LIMIT, DEFAULT_PAGE);

            PageEntity<AdResponseDto> ads = adSearchService.findAll(filter, pageable);

            assertThat(ads.body()).isEmpty();
        }
    }

    @Nested
    class FindAllWithFiltering {

        @Test
        void findAllShouldNotThrowExceptionWhenLocalityIdAndPublisherIdExists() {
            Long localityId = 1L;
            Long publisherId = 2L;
            var filter = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    publisherId,
                    localityId
            );
            var pageable = new PageableRequestDto(DEFAULT_LIMIT, DEFAULT_PAGE);
            doReturn(true).when(localityDao).isExists(localityId);
            doReturn(true).when(userDao).isExists(publisherId);
            doReturn(EMPTY_AD_PAGE).when(adDao).findAll(anyLong(), anyLong(), any(), any());

            assertThatCode(() -> adSearchService.findAll(filter, pageable)).doesNotThrowAnyException();
        }

        @Test
        void findAllShouldNotThrowExceptionWhenLocalityIdAndPublisherIdDoesNotExists() {
            Long localityId = 1L;
            Long publisherId = 2L;
            var filter = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    publisherId,
                    localityId
            );
            var pageable = new PageableRequestDto(DEFAULT_LIMIT, DEFAULT_PAGE);
            doReturn(false).when(localityDao).isExists(localityId);
            doReturn(false).when(userDao).isExists(publisherId);
            doReturn(EMPTY_AD_PAGE).when(adDao).findAll(anyLong(), anyLong(), any(), any());

            assertThatThrownBy(() -> adSearchService.findAll(filter, pageable))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}