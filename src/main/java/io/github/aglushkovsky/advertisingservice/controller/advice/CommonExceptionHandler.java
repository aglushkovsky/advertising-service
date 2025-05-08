package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorObjectDto;
import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Stream;

import static io.github.aglushkovsky.advertisingservice.util.ExceptionHandlerUtils.*;

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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<List<ErrorObjectDto>> handleConstraintViolationException(ConstraintViolationException e) {
        log.info("Start handling ConstraintViolationException", e);

        List<ErrorObjectDto> responseErrors = e.getConstraintViolations().stream()
                .map(constraintViolation -> new ErrorObjectDto(
                        getLastItemFromPath(constraintViolation.getPropertyPath()),
                        constraintViolation.getMessage()
                ))
                .toList();

        ErrorResponseDto<List<ErrorObjectDto>> response = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                responseErrors
        );

        log.info("Finished handling ConstraintViolationException");

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<List<ErrorObjectDto>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("Start handling MethodArgumentNotValidException", e);

        BindingResult bindingResult = e.getBindingResult();

        List<ErrorObjectDto> responseErrors = Stream.concat(
                        bindingResult.getFieldErrors()
                                .stream()
                                .map(fieldError -> new ErrorObjectDto(
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage()
                                )),
                        bindingResult.getGlobalErrors()
                                .stream()
                                .map(objectError -> new ErrorObjectDto(
                                        null,
                                        objectError.getDefaultMessage()
                                ))
                )
                .toList();

        ErrorResponseDto<List<ErrorObjectDto>> response = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                responseErrors
        );

        log.info("Finished handling MethodArgumentNotValidException");

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto<String> handleException(Exception e) {
        log.info("Start handling Exception", e);

        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                "Что-то пошло не так"
        );

        log.info("Finished handling Exception");

        return response;
    }
}
