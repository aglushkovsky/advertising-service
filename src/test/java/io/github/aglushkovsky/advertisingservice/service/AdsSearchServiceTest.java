package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.page.AdPageMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.aglushkovsky.advertisingservice.util.AdServicesTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsSearchServiceTest {

    private static final Long DEFAULT_LIMIT = 50L;

    private static final Long DEFAULT_PAGE = 1L;

    @Mock
    private AdDao adDao;

    @Mock
    private LocalityDao localityDao;

    @Mock
    private UserDao userDao;

    @Mock
    private AdPageMapper adPageMapper;

    @InjectMocks
    private AdsSearchService adsSearchService;

    @Nested
    class FindAll {

        @Test
        void findAllShouldReturnAdsListWhenAllParametersAreValid() {
            Long publisherId = 1L;
            Long localityId = 1L;
            FindAllAdsFilterRequestDto filter = FindAllAdsFilterRequestDto.builder()
                    .term("term")
                    .onlyInTitle(false)
                    .minPrice(10000000L)
                    .maxPrice(20000000L)
                    .publisherId(publisherId)
                    .localityId(localityId)
                    .build();
            PageableRequestDto pageable = PageableRequestDto.builder()
                    .limit(DEFAULT_LIMIT)
                    .page(DEFAULT_PAGE)
                    .build();
            PageEntity<Ad> adStubPageEntityStub = createAdStubPageEntityStub();
            PageEntity<AdResponseDto> adResponseDtoStubPageEntityStub = createAdResponseDtoStubPageEntityStub();
            doReturn(true).when(localityDao).isExists(localityId);
            doReturn(true).when(userDao).isExists(publisherId);
            doReturn(adStubPageEntityStub).when(adDao)
                    .findAll(eq(pageable.limit()), eq(pageable.page()), any(Predicate.class), any(OrderSpecifier[].class));
            doReturn(adResponseDtoStubPageEntityStub).when(adPageMapper).toDtoPage(adStubPageEntityStub);

            PageEntity<AdResponseDto> result = adsSearchService.findAll(filter, pageable);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(adResponseDtoStubPageEntityStub);
            verify(localityDao).isExists(localityId);
            verify(userDao).isExists(publisherId);
            verify(adDao).findAll(eq(pageable.limit()), eq(pageable.page()), any(Predicate.class), any(OrderSpecifier[].class));
        }

        @Test
        void findAllShouldThrowExceptionWhenLocalityIdDoesNotExists() {
            Long publisherId = 1L;
            Long localityId = 1L;
            FindAllAdsFilterRequestDto filter = FindAllAdsFilterRequestDto.builder()
                    .term("term")
                    .onlyInTitle(false)
                    .minPrice(10000000L)
                    .maxPrice(20000000L)
                    .publisherId(publisherId)
                    .localityId(localityId)
                    .build();
            PageableRequestDto pageable = PageableRequestDto.builder()
                    .limit(DEFAULT_LIMIT)
                    .page(DEFAULT_PAGE)
                    .build();
            doReturn(false).when(localityDao).isExists(localityId);

            assertThatThrownBy(() -> adsSearchService.findAll(filter, pageable))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void findAllShouldThrowExceptionWhenPublisherIdDoesNotExists() {
            Long publisherId = 1L;
            Long localityId = 1L;
            FindAllAdsFilterRequestDto filter = FindAllAdsFilterRequestDto.builder()
                    .term("term")
                    .onlyInTitle(false)
                    .minPrice(10000000L)
                    .maxPrice(20000000L)
                    .publisherId(publisherId)
                    .localityId(localityId)
                    .build();
            PageableRequestDto pageable = PageableRequestDto.builder()
                    .limit(DEFAULT_LIMIT)
                    .page(DEFAULT_PAGE)
                    .build();
            doReturn(true).when(localityDao).isExists(localityId);
            doReturn(false).when(userDao).isExists(publisherId);

            assertThatThrownBy(() -> adsSearchService.findAll(filter, pageable))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class GetAdsHistoryByUserId {

        @Test
        void getAdsHistoryByUserIdShouldReturnResultWhenAllParametersAreValid() {
            Long userId = 1L;
            PageableRequestDto pageable = PageableRequestDto.builder()
                    .limit(DEFAULT_LIMIT)
                    .page(DEFAULT_PAGE)
                    .build();
            PageEntity<Ad> adStubPageEntityStub = createAdStubPageEntityStub();
            PageEntity<AdResponseDto> adResponseDtoStubPageEntityStub = createAdResponseDtoStubPageEntityStub();
            doReturn(true).when(userDao).isExists(userId);
            doReturn(adStubPageEntityStub).when(adDao)
                    .findAll(eq(pageable.limit()), eq(pageable.page()), any(Predicate.class), any(OrderSpecifier[].class));
            doReturn(adResponseDtoStubPageEntityStub).when(adPageMapper).toDtoPage(adStubPageEntityStub);

            PageEntity<AdResponseDto> result = adsSearchService.getAdsHistoryByUserId(userId, pageable);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(adResponseDtoStubPageEntityStub);
            verify(userDao).isExists(userId);
            verify(adDao).findAll(eq(pageable.limit()), eq(pageable.page()), any(Predicate.class), any(OrderSpecifier[].class));
            verify(adPageMapper).toDtoPage(adStubPageEntityStub);
            verifyNoInteractions(localityDao);
        }

        @Test
        void getAdsHistoryByUserIdShouldReturnResultWhenUserIdIsInvalid() {
            Long userId = 1L;
            PageableRequestDto pageable = PageableRequestDto.builder()
                    .limit(DEFAULT_LIMIT)
                    .page(DEFAULT_PAGE)
                    .build();
            doReturn(false).when(userDao).isExists(userId);

            assertThatThrownBy(() -> adsSearchService.getAdsHistoryByUserId(userId, pageable))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}