package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.annotation.ServiceUnitTest;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ServiceUnitTest(value = AuthService.class)
class AuthServiceTest {

    private static final Map<String, String> userStubPasswordsHashes = Map.of("qwerty12345",
            "$2a$12$sGu145CigoPn4HWjLOndX.QenSaUBRZreuex2fdo3wtUriQ9dBwie");

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDao userDao;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenUserExistsAndPasswordAreCorrect() {
        String login = "test_user";
        String password = "qwerty12345";
        User userStub = new User(
                1L,
                login,
                userStubPasswordsHashes.get(password),
                null,
                null,
                Role.USER,
                0.0,
                emptyList(),
                emptyList(),
                emptyList()
        );
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(login, password);
        doReturn(Optional.of(userStub)).when(userDao).findByLogin(login);
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());
        doReturn(userStubPasswordsHashes.get(password)).when(jwtUtils).generateAccessToken(any(User.class));

        JwtAuthResponseDto response = authService.login(jwtAuthRequestDto);

        assertThat(response.getAccessToken()).isEqualTo(userStubPasswordsHashes.get(password));
        verify(userDao).findByLogin(login);
        verify(passwordEncoder).matches(anyString(), anyString());
        verify(jwtUtils).generateAccessToken(any(User.class));
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenUserDoesNotExist() {
        String invalidLogin = "test_invalid_login";
        String password = "qwerty12345";
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(invalidLogin, password);
        doReturn(Optional.empty()).when(userDao).findByLogin(invalidLogin);

        assertThatThrownBy(() -> authService.login(jwtAuthRequestDto)).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenPasswordAreIncorrect() {
        String login = "test_user";
        String incorrectPassword = "incorrect_password";
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(login, incorrectPassword);
        User userStub = new User(
                1L,
                login,
                userStubPasswordsHashes.get("qwerty12345"),
                null,
                null,
                Role.USER,
                0.0,
                emptyList(),
                emptyList(),
                emptyList()
        );
        doReturn(Optional.of(userStub)).when(userDao).findByLogin(login);
        doReturn(false).when(passwordEncoder).matches(anyString(), anyString());

        assertThatThrownBy(() -> authService.login(jwtAuthRequestDto)).isInstanceOf(BadCredentialsException.class);
    }
}