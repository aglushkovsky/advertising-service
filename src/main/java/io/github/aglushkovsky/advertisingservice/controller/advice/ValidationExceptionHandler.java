package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorObjectDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.aglushkovsky.advertisingservice.util.ExceptionHandlerUtils.getLastItemFromPath;
import static java.util.Collections.emptyList;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException e) {
        List<ErrorObjectDto> responseErrors = e.getConstraintViolations().stream()
                .map(constraintViolation -> new ErrorObjectDto(
                        getLastItemFromPath(constraintViolation.getPropertyPath()),
                        List.of(constraintViolation.getMessage())
                ))
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Constraint Violation Error");
        problemDetail.setProperty("errors", responseErrors);

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
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

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("errors", responseErrors);

        return new ResponseEntity<>(problemDetail, headers, HttpStatus.BAD_REQUEST);
    }
}
