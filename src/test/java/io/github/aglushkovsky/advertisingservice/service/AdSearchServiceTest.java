package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.AdDao;
import io.github.aglushkovsky.advertisingservice.dao.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dao.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapperImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {AdSearchService.class, AdMapperImpl.class})
class AdSearchServiceTest {

    @Autowired
    private AdSearchService adSearchService;

    @Autowired
    private AdMapper adMapper;

    @MockitoBean
    private AdDao adDao;

    @MockitoBean
    private LocalityDao localityDao;

    @MockitoBean
    private UserDao userDao;

    @Nested
    class FilterByPriceRange {

        @Test
        void findAllShouldNotThrowExceptionWhenPriceRangeIsValidAndMinPriceStrictlyGreaterThanMaxPrice() {
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    new BigDecimal("0"),
                    new BigDecimal("500"),
                    null,
                    null,
                    50L,
                    1L);

            assertThatNoException().isThrownBy(() -> adSearchService.findAll(findAllAdsFilterRequestDto));
        }

        @Test
        void findAllShouldNotThrowExceptionWhenPriceRangeIsValidAndMinPriceEqualsMaxPrice() {
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    new BigDecimal("100"),
                    new BigDecimal("100"),
                    null,
                    null,
                    50L,
                    1L);

            assertThatNoException().isThrownBy(() -> adSearchService.findAll(findAllAdsFilterRequestDto));
        }
    }

    @Nested
    class ParameterDbValidation {

        @Test
        void findAllShouldReturnResultWhenLocalityIdNotNullAndItExists() {
            Long localityId = 1L;
            doReturn(true).when(localityDao).isExists(localityId);
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    null,
                    localityId,
                    50L,
                    1L);

            assertThatCode(() -> adSearchService.findAll(findAllAdsFilterRequestDto)).doesNotThrowAnyException();
        }

        @Test
        void findAllShouldThrowExceptionWhenLocalityIdNotNullAndItDoesNotExists() {
            Long localityId = 1L;
            doReturn(false).when(localityDao).isExists(localityId);
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    null,
                    localityId,
                    50L,
                    1L);

            assertThatThrownBy(() -> adSearchService.findAll(findAllAdsFilterRequestDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void findAllShouldReturnResultWhenPublisherIdNotNullAndItExists() {
            Long publisherId = 1L;
            doReturn(true).when(userDao).isExists(publisherId);
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    publisherId,
                    null,
                    50L,
                    1L);

            assertThatCode(() -> adSearchService.findAll(findAllAdsFilterRequestDto)).doesNotThrowAnyException();
        }

        @Test
        void findAllShouldThrowExceptionWhenPublisherIdNotNullAndItDoesNotExists() {
            Long publisherId = 1L;
            doReturn(false).when(userDao).isExists(publisherId);
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    publisherId,
                    null,
                    50L,
                    1L);

            assertThatThrownBy(() -> adSearchService.findAll(findAllAdsFilterRequestDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void findAllShouldReturnResultWhenPublisherIdNotNullAndPublisherIdNotNullAndTheyExist() {
            Long publisherId = 1L;
            Long localityId = 1L;
            doReturn(true).when(localityDao).isExists(localityId);
            doReturn(true).when(userDao).isExists(publisherId);
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    null,
                    false,
                    null,
                    null,
                    publisherId,
                    localityId,
                    50L,
                    1L);

            assertThatCode(() -> adSearchService.findAll(findAllAdsFilterRequestDto)).doesNotThrowAnyException();
        }
    }

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
            var findAllAdsFilterRequestDto = new FindAllAdsFilterRequestDto(
                    term,
                    true,
                    null,
                    null,
                    null,
                    null,
                    50L,
                    1L);
            doReturn(List.of(ad)).when(adDao).findAll(anyLong(), anyLong(), any(), any());

            List<AdResponseDto> ads = adSearchService.findAll(findAllAdsFilterRequestDto);

            assertThat(ads).hasSize(1);
            assertThat(ads.stream().findFirst().map(AdResponseDto::title).orElseThrow()).containsIgnoringCase(term);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void findAllShouldReturnEmptyListWhenThereAreNoMatchesForTermInTitleAndDescription(Boolean onlyInTitle) {
            doReturn(List.of()).when(adDao).findAll(anyLong(), anyLong(), any(), any());
            FindAllAdsFilterRequestDto filter = new FindAllAdsFilterRequestDto(
                    "dasddsad",
                    onlyInTitle,
                    null,
                    null,
                    null,
                    null,
                    50L,
                    1L);

            List<AdResponseDto> ads = adSearchService.findAll(filter);

            assertThat(ads).isEmpty();
        }
    }
}