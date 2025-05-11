package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto<String> handleNotFoundException(NotFoundException e) {
        log.info("Start handling NotFoundException", e);

        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.NOT_FOUND.value(),
                "Элемент не найден"
        );

        log.info("Finished handling NotFoundException");

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto<String> handleException(Exception e) {
        log.info("Start handling Exception", e);

        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Что-то пошло не так"
        );

        log.info("Finished handling Exception");

        return response;
    }
}
