package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static io.github.aglushkovsky.advertisingservice.util.UserTestUtils.createUserStub;

@UtilityClass
public class UserRateServiceTestUtils {

    public static UserRateCreateRequestDto createUserRateCreateRequestDtoStub() {
        return UserRateCreateRequestDto.builder()
                .value(5.0)
                .build();
    }

    public static UserRate createUserRateStub(Long id) {
        return UserRate.builder()
                .id(id)
                .recipient(createUserStub())
                .value(5.0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
