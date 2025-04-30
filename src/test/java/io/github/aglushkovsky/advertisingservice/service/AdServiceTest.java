package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdServiceTest {

    @Autowired
    private AdService adService;

    @MockitoBean
    private AdDao adDao;

    @Nested
    class FindAllByFilter {

        // FIXME Надо запретить создавать объявления с пустым адресом.
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

            List<AdResponseDto> ads = adService.findAll(findAllAdsFilterRequestDto);

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

            List<AdResponseDto> ads = adService.findAll(filter);

            assertThat(ads).isEmpty();
        }
    }
}