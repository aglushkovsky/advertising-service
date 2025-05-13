package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.AddUserRateToYourselfException;
import io.github.aglushkovsky.advertisingservice.exception.UserRateAlreadyExistsException;
import io.github.aglushkovsky.advertisingservice.service.UserRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{id}/rates")
@RequiredArgsConstructor
@Slf4j
public class UserRateController {

    private final UserRateService userRateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRateResponseDto createUserRate(@PathVariable(name = "id") Long recipientId,
                                              @RequestBody UserRateCreateRequestDto userRateCreateRequestDto) {
        log.info("Start POST /api/v1/users/{}/rates; {}", recipientId, userRateCreateRequestDto);
        UserRateResponseDto response = userRateService.createUserRate(recipientId, userRateCreateRequestDto);
        log.info("Finished POST /api/v1/users/{}/rates; created rated with id", recipientId);
        return response;
    }

    @ExceptionHandler(UserRateAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto<String> handleUserRateAlreadyExists() {
        return new ErrorResponseDto<>(
                HttpStatus.CONFLICT.value(),
                "Вы уже оставляли оценку этому пользователю"
        );
    }

    @ExceptionHandler(AddUserRateToYourselfException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponseDto<String> handleAddUserRateToYourselfException() {
        return new ErrorResponseDto<>(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Оставлять оценку для самого себя недопустимо"
        );
    }
}
