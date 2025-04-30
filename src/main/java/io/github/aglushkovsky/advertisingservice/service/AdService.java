package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.util.PredicateChainBuilder;
import io.github.aglushkovsky.advertisingservice.dao.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.FindAllAdsFilterRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.mapper.AdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.aglushkovsky.advertisingservice.entity.QAd.*;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdDao adDao;

    private final AdMapper adMapper;

    public List<AdResponseDto> findAll(FindAllAdsFilterRequestDto filterRequestDto) {
        Predicate predicate = buildPredicate(filterRequestDto);
        OrderSpecifier<Boolean> order = getDefaultPromotedAdsPrecedenceOrder();
        Long limit = filterRequestDto.limit();
        Long page = filterRequestDto.page();
        Long offset = calculateOffset(limit, page);

        List<Ad> ads = filterRequestDto.localityId() == null
                ? adDao.findAll(limit, offset, predicate, order)
                : adDao.findAll(limit, offset, filterRequestDto.localityId(), predicate, order);

        return ads.stream().map(adMapper::toDto).toList();
    }

    private Predicate buildPredicate(FindAllAdsFilterRequestDto filterRequestDto) {
        return PredicateChainBuilder.builder()
                .and(getTermParameter(filterRequestDto.term()),
                        getSearchByTermPredicateFunction(filterRequestDto.onlyInTitle()))
                .and(filterRequestDto.minPrice(), ad.price::gt)
                .and(filterRequestDto.maxPrice(), ad.price::lt)
                .and(filterRequestDto.publisherId(), ad.publisher.id::eq)
                .build();
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

    private Long calculateOffset(Long limit, Long page) {
        return (page - 1) * limit;
    }

    public Optional<AdResponseDto> findById(Long id) {
        return adDao.findById(id).map(adMapper::toDto);
    }
}
