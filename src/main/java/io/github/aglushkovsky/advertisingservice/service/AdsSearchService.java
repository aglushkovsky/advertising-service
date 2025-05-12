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

import java.util.function.Function;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static io.github.aglushkovsky.advertisingservice.entity.QAd.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocalityPart.localityPart;
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

        Predicate predicate = buildPredicate(filter);
        OrderSpecifier<Boolean> order = getDefaultPromotedAdsPrecedenceOrder();

        PageEntity<Ad> resultPage = adDao.findAll(pageable.limit(), pageable.page(), predicate, order);
        log.info("Finished findAll; found items: {}, filter: {}", resultPage.body().size(), filter);
        return adPageMapper.toDtoPage(resultPage);
    }

    private Predicate buildPredicate(FindAllAdsFilterRequestDto filterRequestDto) {
        return PredicateChainBuilder.builder()
                .and(filterRequestDto.localityId(), getLocalityIdExistsInLocalityAncestorsPredicate())
                .and(filterRequestDto.term(), getSearchByTermPredicateFunction(filterRequestDto.onlyInTitle()))
                .and(filterRequestDto.minPrice(), ad.price::gt)
                .and(filterRequestDto.maxPrice(), ad.price::lt)
                .and(filterRequestDto.publisherId(), ad.publisher.id::eq)
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

    private Function<String, Predicate> getSearchByTermPredicateFunction(boolean isOnlyInTitle) {
        return param -> isOnlyInTitle
                ? ad.title.containsIgnoreCase(param)
                : ad.title.containsIgnoreCase(param).or(ad.description.containsIgnoreCase(param));
    }
}
