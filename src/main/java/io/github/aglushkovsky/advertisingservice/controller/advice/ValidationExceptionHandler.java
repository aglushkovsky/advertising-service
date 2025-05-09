package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorObjectDto;
import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.aglushkovsky.advertisingservice.util.ExceptionHandlerUtils.getLastItemFromPath;
import static java.util.Collections.emptyList;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<List<ErrorObjectDto>> handleConstraintViolationException(ConstraintViolationException e) {
        log.info("Start handling ConstraintViolationException", e);

        List<ErrorObjectDto> responseErrors = e.getConstraintViolations().stream()
                .map(constraintViolation -> new ErrorObjectDto(
                        getLastItemFromPath(constraintViolation.getPropertyPath()),
                        List.of(constraintViolation.getMessage())
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
                                        Optional.ofNullable(fieldError.getDefaultMessage())
                                                .map(List::of)
                                                .orElse(emptyList())
                                )),
                        bindingResult.getGlobalErrors()
                                .stream()
                                .map(objectError -> new ErrorObjectDto(
                                        null,
                                        Optional.ofNullable(objectError.getDefaultMessage())
                                                .map(List::of)
                                                .orElse(emptyList())
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

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<List<ErrorObjectDto>> handleMethodValidationException(HandlerMethodValidationException e) {
        log.info("Start handling MethodValidationException", e);

        List<ErrorObjectDto> responseErrors = e.getParameterValidationResults()
                .stream()
                .map(result -> new ErrorObjectDto(
                        result.getMethodParameter().getParameterName(),
                        result.getResolvableErrors()
                                .stream()
                                .map(MessageSourceResolvable::getDefaultMessage)
                                .toList()
                ))
                .toList();

        log.info("Finished handling MethodValidationException");

        return new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                responseErrors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<ErrorObjectDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("Start handling MethodArgumentTypeMismatchException", e);

        ErrorResponseDto<ErrorObjectDto> response = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                new ErrorObjectDto(
                        e.getPropertyName(),
                        List.of("Некорректный формат параметра")
                )
        );

        log.info("Finished handling MethodArgumentTypeMismatchException");

        return response;
    }
}
