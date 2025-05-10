package io.github.aglushkovsky.advertisingservice.controller.advice;

import io.github.aglushkovsky.advertisingservice.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SecurityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.info("Start handling UsernameNotFoundException", e);
        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "По введённому логину пользователь не найден"
        );
        log.info("Finished handling UsernameNotFoundException");
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto<String> handleBadCredentialsException(BadCredentialsException e) {
        log.info("Start handling BadCredentialsException", e);
        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Неверный пароль"
        );
        log.info("Finished handling BadCredentialsException");
        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto<String> handleAccessDeniedException(AccessDeniedException e) {
        log.info("Start handling AccessDeniedException", e);
        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.FORBIDDEN.value(),
                "Доступ запрещён"
        );
        log.info("Finished handling AccessDeniedException");
        return response;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto<String> handleAuthenticationException(AuthenticationException e) {
        log.info("Start handling AuthenticationException", e);
        ErrorResponseDto<String> response = new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Ошибка аутентификации"
        );
        log.info("Finished handling AuthenticationException");
        return response;
    }
}
