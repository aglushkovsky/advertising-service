package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;

@Tag(name = "User", description = "CRUD operations for users")
public interface UserControllerDocs {

    @SecurityRequirements
    UserResponseDto createUser(@Validated({Default.class, CreateGroup.class})
                               UserCreateEditRequestDto userCreateEditRequestDto);

    UserResponseDto editUser(@Min(1) Long id, @Validated(Default.class)
                             UserCreateEditRequestDto userCreateEditRequestDto);

    @SecurityRequirements
    UserResponseDto getUserById(@Min(1) Long id);
}
