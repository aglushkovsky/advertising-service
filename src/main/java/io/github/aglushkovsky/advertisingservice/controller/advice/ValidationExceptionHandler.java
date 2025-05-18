package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorObjectDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static io.github.aglushkovsky.advertisingservice.util.ExceptionHandlerUtils.getLastItemFromPath;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException e) {
        List<ErrorObjectDto> responseErrors = e.getConstraintViolations().stream()
                .map(this::convertFromConstraintViolationToErrorObjectDto)
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Constraint Violation Error");
        problemDetail.setProperty("errors", responseErrors);

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorObjectDto> errors = e.getBindingResult().getAllErrors().stream()
                .map(err -> {
                    if (err.contains(ConstraintViolation.class)) {
                        ConstraintViolation<?> unwrapped = err.unwrap(ConstraintViolation.class);
                        return convertFromConstraintViolationToErrorObjectDto(unwrapped);
                    }
                    if (err instanceof FieldError fieldError) {
                        return new ErrorObjectDto(fieldError.getField(), "Invalid value");
                    }
                    return new ErrorObjectDto(null, err.getDefaultMessage());
                })
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("errors", errors);

        return new ResponseEntity<>(problemDetail, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException
            (HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorObjectDto> errors = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(err -> result.unwrap(err, ConstraintViolation.class)))
                .map(this::convertFromConstraintViolationToErrorObjectDto)
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("errors", errors);

        return new ResponseEntity<>(problemDetail, headers, HttpStatus.BAD_REQUEST);
    }

    private ErrorObjectDto convertFromConstraintViolationToErrorObjectDto(ConstraintViolation<?> constraintViolation) {
        String lastItemFromPath = getLastItemFromPath(constraintViolation.getPropertyPath());
        return new ErrorObjectDto(
                StringUtils.hasText(lastItemFromPath) ? lastItemFromPath : null,
                constraintViolation.getMessage()
        );
    }
}
