package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import io.github.aglushkovsky.advertisingservice.mapper.AdPageMapper;
import io.github.aglushkovsky.advertisingservice.util.PredicateChainBuilder;
import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.function.Function;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static io.github.aglushkovsky.advertisingservice.entity.QAd.*;
import static io.github.aglushkovsky.advertisingservice.entity.QLocalityPart.localityPart;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdSearchService {

    private final AdDao adDao;
    private final AdMapper adMapper;
    private final AdPageMapper adPageMapper;

    public PageEntity<AdResponseDto> findAll(FindAllAdsFilterRequestDto filter, PageableRequestDto pageable) {
        log.info("Start findAll; filter: {}", filter);

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

    public AdResponseDto findById(Long id) {
        log.info("Start findById; id={}", id);
        return adDao.findById(id).map(adMapper::toDto).orElseThrow(() -> {
            log.error("Could not find ad with id: {}", id);
            return new NotFoundException(id);
        });
    }
}
