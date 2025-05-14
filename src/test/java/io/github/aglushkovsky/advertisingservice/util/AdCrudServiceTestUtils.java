package io.github.aglushkovsky.advertisingservice.util;

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
import static io.github.aglushkovsky.advertisingservice.entity.enumeration.Role.USER;

@UtilityClass
public class AdCrudServiceTestUtils {

    public static User createUserStub() {
        return User.builder()
                .id(1L)
                .login("test_user")
                .passwordHash("password_hash")
                .email(null)
                .phoneNumber(null)
                .role(USER)
                .totalRating(0.0)
                .build();
    }

    public static Ad createAdStub(Long adId) {
        User user = createUserStub();
        return Ad.builder()
                .id(adId)
                .title("ad title")
                .price(12345L)
                .description(null)
                .locality(Locality.builder()
                        .id(1L)
                        .name("Test City")
                        .type(CITY)
                        .build())
                .publisher(user)
                .publishedAt(LocalDateTime.parse("2025-05-12T13:51:48.899862700"))
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
}
