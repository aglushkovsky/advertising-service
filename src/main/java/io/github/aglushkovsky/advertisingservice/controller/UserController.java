package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.UserControllerDocs;
import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserCreateEditRequestDto userCreateEditRequestDto) {
        log.info("Start POST /api/v1/registration");
        UserResponseDto response = userService.createUser(userCreateEditRequestDto);
        log.info("Finished POST /api/v1/registration; created user with id={}", response.id());
        return response;
    }

    @PatchMapping("/users/{id}")
    public UserResponseDto editUser(@PathVariable Long id,
                                    @RequestBody UserCreateEditRequestDto userCreateEditRequestDto) {
        log.info("Start PATCH /api/v1/user/{}/edit", id);
        UserResponseDto response = userService.editUser(id, userCreateEditRequestDto);
        log.info("Finished PATCH /api/v1/user/{}/edit", id);
        return response;
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        log.info("Start GET /api/v1/user/{}", id);
        UserResponseDto response = userService.findById(id);
        log.info("Finished GET /api/v1/user/{}", id);
        return response;
    }
}
