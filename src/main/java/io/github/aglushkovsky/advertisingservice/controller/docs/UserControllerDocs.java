package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "CRUD operations for users")
public interface UserControllerDocs {

    @SecurityRequirements
    UserResponseDto createUser(UserCreateEditRequestDto userCreateEditRequestDto);

    UserResponseDto editUser(Long id, UserCreateEditRequestDto userCreateEditRequestDto);

    @SecurityRequirements
    UserResponseDto getUserById(Long id);
}
