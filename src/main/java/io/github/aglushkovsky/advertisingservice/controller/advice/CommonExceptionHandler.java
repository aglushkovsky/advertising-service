package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto<String> handleNotFoundException() {
        log.info("Start handling NotFoundException");
        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.NOT_FOUND.value(),
                "Элемент не найден");
        log.info("Finished handling NotFoundException");
        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.info("Start handling ConstraintViolationException");
        ErrorResponseDto<List<String>> response = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList()
        );
        log.info("Finished handling ConstraintViolationException");
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("Start handling MethodArgumentNotValidException");
        ErrorResponseDto<List<String>> response = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getBindingResult().getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList()
        );
        log.info("Finished handling MethodArgumentNotValidException");
        return response;
    }
}
