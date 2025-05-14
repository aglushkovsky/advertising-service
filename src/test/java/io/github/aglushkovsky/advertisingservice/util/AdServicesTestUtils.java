package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.response.AdResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.LocalityResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Ad;
import io.github.aglushkovsky.advertisingservice.entity.Locality;
import io.github.aglushkovsky.advertisingservice.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

import static io.github.aglushkovsky.advertisingservice.entity.enumeration.AdStatus.ACTIVE;
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType.CITY;
import static io.github.aglushkovsky.advertisingservice.util.PageableTestCommonUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.UserServiceTestUtils.createUserStub;

@UtilityClass
public class AdServicesTestUtils {

    private static final Long DEFAULT_ID = 1L;

    public static Ad createAdStub(Long adId) {
        User user = createUserStub();
        return Ad.builder()
                .id(adId)
                .title("ad title")
                .price(12345L)
                .description(null)
                .locality(Locality.builder()
                        .id(DEFAULT_ID)
                        .name("Test City")
                        .type(CITY)
                        .build())
                .publisher(user)
                .publishedAt(LocalDateTime.now())
                .status(ACTIVE)
                .isPromoted(false)
                .build();
    }

    public static AdResponseDto createAdStubResponseDto(Long adId) {
        Ad adStub = createAdStub(adId);
        User user = createUserStub();
        return AdResponseDto.builder()
                .id(adId)
                .title(adStub.getTitle())
                .price(adStub.getPrice())
                .description(adStub.getDescription())
                .localityParts(List.of(
                        LocalityResponseDto.builder()
                                .id(adStub.getLocality().getId())
                                .name(adStub.getLocality().getName())
                                .type(adStub.getLocality().getType().name())
                                .build()
                ))
                .publisher(
                        UserResponseDto.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .phoneNumber(user.getPhoneNumber())
                                .email(user.getEmail())
                                .totalRating(user.getTotalRating())
                                .build()
                )
                .publishedAt(adStub.getPublishedAt().toString())
                .status(adStub.getStatus().name())
                .isPromoted(adStub.getIsPromoted())
                .build();
    }

    public static PageEntity<Ad> createAdStubPageEntityStub() {
        return createPageEntityStubWithSingleRecord(createAdStub(DEFAULT_ID));
    }

    public static PageEntity<AdResponseDto> createAdResponseDtoStubPageEntityStub() {
        return createPageEntityStubWithSingleRecord(createAdStubResponseDto(DEFAULT_ID));
    }
}
