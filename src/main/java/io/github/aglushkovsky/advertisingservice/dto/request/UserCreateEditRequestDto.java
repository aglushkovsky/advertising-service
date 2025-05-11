package io.github.aglushkovsky.advertisingservice.dto.request;

import io.github.aglushkovsky.advertisingservice.validator.group.user.create.UserCreateGroup;
import jakarta.validation.constraints.*;

public record UserCreateEditRequestDto(@NotBlank(groups = UserCreateGroup.class) @Size(min = 5, max = 50) String login,
                                       @NotBlank(groups = UserCreateGroup.class) String password,
                                       @Email @Size(min = 5, max = 50) String email,
                                       @Pattern(regexp = "^(?:\\+7|8)\\d{10}$") String phoneNumber) {
}
