package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.UserRateControllerDocs;
import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.AddUserRateToYourselfException;
import io.github.aglushkovsky.advertisingservice.exception.UserRateAlreadyExistsException;
import io.github.aglushkovsky.advertisingservice.service.UserRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{id}/rates")
@RequiredArgsConstructor
@Slf4j
public class UserRateController implements UserRateControllerDocs {

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
    public ResponseEntity<ProblemDetail> handleUserRateAlreadyExists() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("User rate already exists");
        problemDetail.setDetail("Вы уже оставляли оценку этому пользователю");

        return new ResponseEntity<>(problemDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AddUserRateToYourselfException.class)
    public ResponseEntity<ProblemDetail> handleAddUserRateToYourselfException() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Attempt to add user rate to yourself");
        problemDetail.setDetail("Оставлять оценку для самого себя недопустимо");

        return new ResponseEntity<>(problemDetail, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
