package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 3)
public class DaoExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = getUserFriendlyMessage(e.getCause());
        return new ErrorResponseDto<>(
                HttpStatus.CONFLICT.value(),
                message
        );
    }

    @SneakyThrows
    private String getUserFriendlyMessage(Throwable e) {
        if (e instanceof ConstraintViolationException cve) {
            return getUserFriendlyMessage(cve);
        }
        return "Ошибка при запросе в БД";
    }

    private String getUserFriendlyMessage(ConstraintViolationException e) {
        return switch (e.getConstraintName()) {
            case "user_login_key" -> "Логин занят другим пользователем";
            case "user_email_key" -> "Email занят другим пользователем";
            case "user_phone_number_key" -> "Номер телефона был введён другим пользователем";
            default -> "Были введены некорректные данные";
        };
    }
}
