package io.github.aglushkovsky.advertisingservice.integration.controller;

import com.jayway.jsonpath.JsonPath;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import io.github.aglushkovsky.advertisingservice.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenUserExistsAndPasswordAreCorrect() throws Exception {
        String authRequest = """
                {
                  "login": "test_user",
                  "password": "qwerty12345"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        String accessToken = JsonPath.parse(contentAsString).read("$.accessToken");

        assertThatCode(() -> jwtUtils.validateAccessToken(accessToken)).doesNotThrowAnyException();
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenUserDoesNotExist() throws Exception {
        String authRequest = """
                {
                  "login": "incorrect_login",
                  "password": "qwerty12345"
                }
                """;

        mockMvc.perform(post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginShouldReturnJwtAuthenticationTokenWhenPasswordAreIncorrect() throws Exception {
        String authRequest = """
                {
                  "login": "test_user",
                  "password": "incorrect_password"
                }
                """;

        mockMvc.perform(post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isUnauthorized());
    }
}
