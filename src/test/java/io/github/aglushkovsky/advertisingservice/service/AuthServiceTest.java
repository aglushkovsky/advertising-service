package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final Map<String, String> userStubPasswordsHashes = Map.of("qwerty12345",
            "$2a$12$sGu145CigoPn4HWjLOndX.QenSaUBRZreuex2fdo3wtUriQ9dBwie");

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenUserByLoginExistsAndPasswordAreCorrect() {
        String login = "test_user";
        String password = "qwerty12345";
        User userStub = createUserStubWithHashedPassword(login, password);
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
    void loginShouldReturnJwtAuthenticationTokenWhenUserByLoginDoesNotExist() {
        String invalidLogin = "test_invalid_login";
        String password = "qwerty12345";
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(invalidLogin, password);
        doReturn(Optional.empty()).when(userDao).findByLogin(invalidLogin);

        assertThatThrownBy(() -> authService.login(jwtAuthRequestDto))
                .isInstanceOf(UsernameNotFoundException.class);
        verify(userDao).findByLogin(invalidLogin);
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenPasswordAreIncorrect() {
        String login = "test_user";
        String incorrectPassword = "incorrect_password";
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(login, incorrectPassword);
        User userStub = createUserStubWithHashedPassword(login, incorrectPassword);
        doReturn(Optional.of(userStub)).when(userDao).findByLogin(login);
        doReturn(false).when(passwordEncoder).matches(incorrectPassword, userStub.getPasswordHash());

        assertThatThrownBy(() -> authService.login(jwtAuthRequestDto))
                .isInstanceOf(BadCredentialsException.class);
        verify(userDao).findByLogin(login);
        verify(passwordEncoder).matches(incorrectPassword, userStub.getPasswordHash());
    }

    private static User createUserStubWithHashedPassword(String login, String password) {
        return User.builder()
                .id(1L)
                .login(login)
                .passwordHash(userStubPasswordsHashes.get(password))
                .email(null)
                .phoneNumber(null)
                .role(Role.USER)
                .totalRating(0.0)
                .build();
    }
}