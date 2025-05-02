package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.aglushkovsky.advertisingservice.dao.LocalityDao;
import io.github.aglushkovsky.advertisingservice.dao.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.util.PredicateChainBuilder;
import io.github.aglushkovsky.advertisingservice.dao.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
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
    private final LocalityDao localityDao;
    private final UserDao userDao;
    private final AdMapper adMapper;

    @Transactional(readOnly = true)
    public List<AdResponseDto> findAll(FindAllAdsFilterRequestDto filterRequestDto) {
        log.info("Start findAll; filterRequestDto: {}", filterRequestDto);

        if (isNotExists(filterRequestDto.localityId(), localityDao::isExists)) {
            log.error("Not found locality with id {}", filterRequestDto.localityId());
            throw new NotFoundException(filterRequestDto.localityId());
        }
        if (isNotExists(filterRequestDto.publisherId(), userDao::isExists)) {
            log.error("Not found publisher with id {}", filterRequestDto.publisherId());
            throw new NotFoundException(filterRequestDto.publisherId());
        }

        Predicate predicate = buildPredicate(filterRequestDto);
        OrderSpecifier<Boolean> order = getDefaultPromotedAdsPrecedenceOrder();
        Long limit = filterRequestDto.limit();
        Long page = filterRequestDto.page();

        List<Ad> result = adDao.findAll(limit, page, predicate, order);
        log.info("End findAll; found items: {} filterRequestDto: {}", result.size(), filterRequestDto);

        return result.stream().map(adMapper::toDto).toList();
    }

    private boolean isNotExists(Long id, Function<Long, Boolean> isExistsFunction) {
        return id != null && !isExistsFunction.apply(id);
    }

    private Predicate buildPredicate(FindAllAdsFilterRequestDto filterRequestDto) {
        return PredicateChainBuilder.builder()
                .and(filterRequestDto.localityId(), getLocalityIdExistsInLocalityAncestorsPredicate())
                .and(getTermParameter(filterRequestDto.term()),
                        getSearchByTermPredicateFunction(filterRequestDto.onlyInTitle()))
                .and(filterRequestDto.minPrice(), ad.price::gt)
                .and(filterRequestDto.maxPrice(), ad.price::lt)
                .and(filterRequestDto.publisherId(), ad.publisher.id::eq)
                .build();
    }

    // TODO Логику создания предикатов лучше вынести в отдельный класс?
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
                ? ad.title.lower().like(param.toLowerCase())
                : ad.title.lower().like(param.toLowerCase()).or(ad.description.lower().like(param.toLowerCase()));
    }

    private String getTermParameter(String term) {
        return term != null ? "%" + term + "%" : null;
    }

    public AdResponseDto findById(Long id) {
        log.info("Start findById; id={}", id);
        return adDao.findById(id).map(adMapper::toDto).orElseThrow(() -> {
            log.error("Could not find ad with id: {}", id);
            return new NotFoundException(id);
        });
    }
}
