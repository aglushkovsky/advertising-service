package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcUnitTest(AuthController.class)
class AuthControllerTest {

    private static final String JWT_TOKEN_STUB = "jwtTokenStub";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenLoginAndPasswordAreCorrect() throws Exception {
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(
                "test_user",
                "test_password"
        );
        JwtAuthResponseDto jwtAuthResponseDto = new JwtAuthResponseDto(JWT_TOKEN_STUB);
        doReturn(jwtAuthResponseDto).when(authService).login(jwtAuthRequestDto);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtAuthRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.accessToken", is(JWT_TOKEN_STUB)));
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenLoginIsIncorrect() throws Exception {
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(
                "incorrect_login",
                "test_password"
        );
        doThrow(UsernameNotFoundException.class).when(authService).login(jwtAuthRequestDto);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtAuthRequestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenPasswordIsIncorrect() throws Exception {
        JwtAuthRequestDto jwtAuthRequestDto = new JwtAuthRequestDto(
                "test_user",
                "incorrect_password"
        );
        doThrow(BadCredentialsException.class).when(authService).login(jwtAuthRequestDto);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtAuthRequestDto)))
                .andExpect(status().isUnauthorized());
    }
}