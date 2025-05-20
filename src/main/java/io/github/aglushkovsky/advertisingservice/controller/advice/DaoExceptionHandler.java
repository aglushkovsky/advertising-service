package io.github.aglushkovsky.advertisingservice.controller.advice;

import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 3)
public class DaoExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Data Integrity Violation");
        problemDetail.setDetail(getUserFriendlyMessage(e.getCause()));

        return new ResponseEntity<>(problemDetail, HttpStatus.CONFLICT);
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
