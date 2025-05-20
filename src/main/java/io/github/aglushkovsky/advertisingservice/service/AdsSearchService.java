package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.mapper.page.AdPageMapper;
import io.github.aglushkovsky.advertisingservice.util.PredicateChainBuilder;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static io.github.aglushkovsky.advertisingservice.entity.QAd.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocalityPart.localityPart;
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.AdStatus.*;
import static io.github.aglushkovsky.advertisingservice.validator.DaoIdValidator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsSearchService {

    private final AdDao adDao;
    private final LocalityDao localityDao;
    private final UserDao userDao;
    private final AdPageMapper adPageMapper;

    public PageEntity<AdResponseDto> findAll(FindAllAdsFilterRequestDto filter, PageableRequestDto pageable) {
        log.info("Start findAll; filter: {}", filter);

        validateId(filter.localityId(), localityDao::isExists, "Not found locality with id={}", true);
        validateId(filter.publisherId(), userDao::isExists, "Not found publisher with id={}", true);

        Predicate predicate = buildFilterPredicate(filter);
        OrderSpecifier<?>[] orders = new OrderSpecifier[]{
                getDefaultPromotedAdsPrecedenceOrder(),
                getDefaultUserTotalRatingOrder()
        };

        PageEntity<Ad> resultPage = adDao.findAll(pageable.limit(), pageable.page(), predicate, orders);
        PageEntity<AdResponseDto> resultDtoPage = adPageMapper.toDtoPage(resultPage);

        log.info("Finished findAll; found items: {}, filter: {}", resultDtoPage.body().size(), filter);

        return resultDtoPage;
    }

    private Predicate buildFilterPredicate(FindAllAdsFilterRequestDto filterRequestDto) {
        return PredicateChainBuilder.builder()
                .and(filterRequestDto.localityId(), getLocalityIdExistsInLocalityAncestorsPredicate())
                .and(filterRequestDto.term(), getSearchByTermPredicateFunction(filterRequestDto.onlyInTitle()))
                .and(filterRequestDto.minPrice(), ad.price::gt)
                .and(filterRequestDto.maxPrice(), ad.price::lt)
                .and(filterRequestDto.publisherId(), ad.publisher.id::eq)
                .and(ACTIVE, ad.status::eq)
                .build();
    }

    private Function<Long, Predicate> getLocalityIdExistsInLocalityAncestorsPredicate() {
        return localityId -> selectFrom(localityPart)
                .where(localityPart.descendantLocality.eq(ad.locality)
                        .and(localityPart.ancestorLocality.id.eq(localityId)))
                .exists();
    }

    private OrderSpecifier<Boolean> getDefaultPromotedAdsPrecedenceOrder() {
        return ad.isPromoted.desc();
    }

    private OrderSpecifier<Double> getDefaultUserTotalRatingOrder() {
        return ad.publisher.totalRating.desc();
    }

    private Function<String, Predicate> getSearchByTermPredicateFunction(boolean isOnlyInTitle) {
        return param -> isOnlyInTitle
                ? ad.title.containsIgnoreCase(param)
                : ad.title.containsIgnoreCase(param).or(ad.description.containsIgnoreCase(param));
    }

    public PageEntity<AdResponseDto> getAdsHistoryByUserId(Long userId, PageableRequestDto pageable) {
        log.info("Start getAdsHistoryByUserId; userId: {}", userId);

        validateId(userId, userDao::isExists, "Not found user with id={}", false);

        Predicate predicate = buildPredicateForGettingUserAdsHistory(userId);
        PageEntity<Ad> userAdsHistoryPage = adDao.findAll(
                pageable.limit(),
                pageable.page(),
                predicate,
                getDefaultPublishingDateOrder()
        );
        PageEntity<AdResponseDto> userDtoAdsHistoryPage = adPageMapper.toDtoPage(userAdsHistoryPage);

        log.info("Finished getAdsHistoryByUserId; userId: {}", userId);

        return userDtoAdsHistoryPage;
    }

    private Predicate buildPredicateForGettingUserAdsHistory(Long userId) {
        return PredicateChainBuilder.builder()
                .and(userId, ad.publisher.id::eq)
                .and(SOLD, ad.status::eq)
                .build();
    }

    private OrderSpecifier<LocalDateTime> getDefaultPublishingDateOrder() {
        return ad.publishedAt.desc();
    }
}
