package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.service.UserService;
import io.github.aglushkovsky.advertisingservice.validator.group.CreateGroup;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    @SecurityRequirements
    public UserResponseDto createUser(@RequestBody @Validated({Default.class, CreateGroup.class})
                                      UserCreateEditRequestDto userCreateEditRequestDto) {
        log.info("Start POST /api/v1/registration");
        UserResponseDto response = userService.createUser(userCreateEditRequestDto);
        log.info("Finished POST /api/v1/registration; created user with id={}", response.id());
        return response;
    }

    @PatchMapping("/users/{id}")
    public UserResponseDto editUser(@PathVariable @Min(1) Long id,
                                    @RequestBody @Validated(Default.class)
                                    UserCreateEditRequestDto userCreateEditRequestDto) {
        log.info("Start PATCH /api/v1/user/{}/edit", id);
        UserResponseDto response = userService.editUser(id, userCreateEditRequestDto);
        log.info("Finished PATCH /api/v1/user/{}/edit", id);
        return response;
    }
}
