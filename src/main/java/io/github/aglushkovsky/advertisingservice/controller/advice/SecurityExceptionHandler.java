package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class SecurityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto<String> handleUsernameNotFoundException() {
        return new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "По введённому логину пользователь не найден"
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto<String> handleBadCredentials() {
        return new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Неверный пароль"
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto<String> handleAccessDeniedException() {
        return new ErrorResponseDto<>(
                HttpStatus.FORBIDDEN.value(),
                "Доступ запрещён"
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto<String> handleAuthenticationException() {
        return new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Ошибка аутентификации"
        );
    }
}
