package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import lombok.experimental.UtilityClass;

import static io.github.aglushkovsky.advertisingservice.entity.enumeration.Role.USER;

@UtilityClass
public class UserServiceTestUtils {

    private static final Long DEFAULT_ID = 1L;

    public static User createUserStub() {
        return createUserStub(DEFAULT_ID);
    }

    public static User createUserStub(Long id) {
        return User.builder()
                .id(id)
                .login("test_user")
                .passwordHash("password_hash")
                .email(null)
                .phoneNumber(null)
                .role(USER)
                .totalRating(0.0)
                .build();
    }

    public static UserResponseDto createUserResponseDtoStub() {
        return UserResponseDto.builder()
                .id(DEFAULT_ID)
                .login("test_user")
                .email(null)
                .phoneNumber(null)
                .totalRating(0.0)
                .build();
    }
}
