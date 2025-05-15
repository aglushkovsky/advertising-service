package io.github.aglushkovsky.advertisingservice.mapper;

import io.github.aglushkovsky.advertisingservice.dao.impl.AdDao;
import io.github.aglushkovsky.advertisingservice.dto.request.AdCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.mapstruct.MappingConstants.ComponentModel.*;
import static org.mapstruct.ReportingPolicy.IGNORE;

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
            expression = "java(localityMapper.toEntityFromLocalityId(adCreateEditRequestDto.localityId()))")
    @Mapping(target = "isPromoted", constant = "false")
    @Mapping(target = "publishedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", constant = "ACTIVE")
    public abstract Ad toEntity(AdCreateEditRequestDto adCreateEditRequestDto);

    @Mapping(target = "locality",
            expression = """
                    java(adCreateEditRequestDto.localityId() == null
                            ? ad.getLocality()
                            : localityMapper.toEntityFromLocalityId(adCreateEditRequestDto.localityId()))
                    """)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Ad editAd(@MappingTarget Ad ad, AdCreateEditRequestDto adCreateEditRequestDto);
}
