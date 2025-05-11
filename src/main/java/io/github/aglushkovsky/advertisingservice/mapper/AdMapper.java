package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(
        componentModel = SPRING,
        uses = LocalityMapper.class
)
@Slf4j
public abstract class AdMapper {

    protected static final int DIGITS_AFTER_POINT = 2;

    @Autowired
    protected AdDao adDao;

    @Mapping(target = "localityParts", source = "locality")
    public abstract AdResponseDto toDto(Ad ad);

    public BigDecimal convertToReadablePrice(BigDecimal price) {
        return price == null ? null : price.movePointLeft(DIGITS_AFTER_POINT);
    }

    public Ad toEntityFromAdId(Long adId) {
        return adDao.findById(adId).orElseThrow(() -> {
            log.error("Could not find ad with id {}", adId);
            return new NotFoundException(adId);
        });
    }
}
