package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.mapstruct.MappingConstants.ComponentModel.*;
import static org.mapstruct.ReportingPolicy.*;

@Mapper(
        componentModel = SPRING,
        uses = LocalityMapper.class,
        imports = {LocalDateTime.class, SecurityUtils.class},
        unmappedTargetPolicy = IGNORE
)
@Slf4j
public abstract class AdMapper {

    @Autowired
    protected AdDao adDao;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected LocalityMapper localityMapper;

    @Mapping(target = "localityParts", source = "locality")
    public abstract AdResponseDto toDto(Ad ad);

    public Ad toEntityFromAdId(Long adId) {
        return adDao.findById(adId).orElseThrow(() -> {
            log.error("Could not find ad with id {}", adId);
            return new NotFoundException(adId);
        });
    }

    @Mapping(target = "publisher",
            expression = "java(userMapper.toUserFromUserId(SecurityUtils.getAuthenticatedUserId()))")
    @Mapping(target = "locality",
            expression = "java(localityMapper.toEntityFromLocalityId(adCreateEditResponseDto.localityId()))")
    @Mapping(target = "isPromoted", constant = "false")
    @Mapping(target = "publishedAt", expression = "java(LocalDateTime.now())")
    public abstract Ad toEntity(AdCreateEditResponseDto adCreateEditResponseDto);

    @Mapping(target = "locality",
            expression = """
                    java(adCreateEditResponseDto.localityId() == null
                            ? ad.getLocality()
                            : localityMapper.toEntityFromLocalityId(adCreateEditResponseDto.localityId()))
                    """)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Ad updateAd(@MappingTarget Ad ad, AdCreateEditResponseDto adCreateEditResponseDto);
}
