package io.github.aglushkovsky.advertisingservice.integration.controller;

import com.jayway.jsonpath.JsonPath;
import io.github.aglushkovsky.advertisingservice.annotation.WithJwtAuthenticationContext;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.integration.AbstractIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Nested
    @WithAnonymousUser
    class CreateUser {

        @Test
        void createUserShouldCreateNewUserWhenConstraintsHaveNotBeenViolated() throws Exception {
            String userCreateRequest = """
                    {
                      "login": "test_new_user",
                      "password": "qwerty12345",
                      "email": "test_new_user@domain.com",
                      "phoneNumber": "+79191234567"
                    }
                    """;

            MvcResult result = mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userCreateRequest))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            Integer id = JsonPath.parse(contentAsString).read("$.id");

            User user = userDao.findById(id.longValue()).orElseThrow();
            assertThat(user.getId()).isEqualTo(id.longValue());
            assertThat(user.getLogin()).isEqualTo("test_new_user");
            assertThat(user.getPasswordHash()).matches(hash -> passwordEncoder.matches("qwerty12345", hash));
            assertThat(user.getEmail()).isEqualTo("test_new_user@domain.com");
            assertThat(user.getPhoneNumber()).isEqualTo("+79191234567");
        }

        @Test
        void createUserShouldReturnConflictResponseWhenLoginUniqueConstraintIsViolated() throws Exception {
            String userCreateRequest = """
                    {
                      "login": "test_user",
                      "password": "qwerty12345",
                      "email": "test_new_user@domain.com",
                      "phoneNumber": "+79191234567"
                    }
                    """;

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userCreateRequest))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
        }

        @Test
        void createUserShouldReturnConflictResponseWhenEmailUniqueConstraintIsViolated() throws Exception {
            String userCreateRequest = """
                    {
                      "login": "test_new_user",
                      "password": "qwerty12345",
                      "email": "example@example.com",
                      "phoneNumber": "+79191234567"
                    }
                    """;

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userCreateRequest))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
        }

        @Test
        void createUserShouldReturnConflictResponseWhenPhoneNumberUniqueConstraintIsViolated() throws Exception {
            String userCreateRequest = """
                    {
                      "login": "test_new_user",
                      "password": "qwerty12345",
                      "email": "test_new_user@domain.com",
                      "phoneNumber": "+79531234567"
                    }
                    """;

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userCreateRequest))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
        }
    }

    @Nested
    class EditUser {

        @Test
        void editUserShouldReturnEditedUserWhenConstraintsHaveNotBeenViolated() throws Exception {
            Long targetUserId = 1L;
            String userEditRequest = """
                    {
                      "login": "test_new_user",
                      "email": "new_example@example.com",
                      "phoneNumber": "+79991234567"
                    }
                    """;

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userEditRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.login", equalTo("test_new_user")))
                    .andExpect(jsonPath("$.email", equalTo("new_example@example.com")))
                    .andExpect(jsonPath("$.phoneNumber", equalTo("+79991234567")));

            User user = userDao.findById(targetUserId).orElseThrow();
            assertThat(user.getLogin()).isEqualTo("test_new_user");
            assertThat(user.getEmail()).isEqualTo("new_example@example.com");
            assertThat(user.getPhoneNumber()).isEqualTo("+79991234567");
        }

        @Test
        @Disabled("Непонятно, как заставить делать коммит транзакции для update")
        void editUserShouldReturnConflictResponseWhenLoginUniqueConstraintIsViolated() throws Exception {
            Long targetUserId = 1L;
            String userEditRequest = """
                    {
                      "login": "test_admin"
                    }
                    """;

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userEditRequest))
                    .andExpect(status().isConflict());

            User user = userDao.findById(targetUserId).orElseThrow();
            assertThat(user.getLogin()).isNotEqualTo("test_admin");
        }

        @Test
        @Disabled("Непонятно, как заставить делать коммит транзакции для update")
        void editUserShouldReturnConflictResponseWhenEmailUniqueConstraintIsViolated() throws Exception {
            Long targetUserId = 1L;
            String userEditRequest = """
                    {
                      "email": "admin_example@example.com"
                    }
                    """;

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userEditRequest))
                    .andExpect(status().isConflict());

            User user = userDao.findById(targetUserId).orElseThrow();
            assertThat(user.getEmail()).isNotEqualTo("admin_example@example.com");
        }

        @Test
        @Disabled("Непонятно, как заставить делать коммит транзакции для update")
        void editUserShouldReturnConflictResponseWhenPhoneNumberUniqueConstraintIsViolated() throws Exception {
            Long targetUserId = 1L;
            String userEditRequest = """
                    {
                      "phoneNumber": "+79202345678"
                    }
                    """;

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userEditRequest))
                    .andExpect(status().isConflict());

            User user = userDao.findById(targetUserId).orElseThrow();
            assertThat(user.getPhoneNumber()).isNotEqualTo("+79202345678");
        }

        @Test
        void editUserShouldReturnNotFoundResponseWhenTargetUserIdDoesNotExist() throws Exception {
            Long targetUserId = 100000L;
            String userEditRequest = "{}";

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userEditRequest))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithJwtAuthenticationContext(id = 10L)
        void editUserShouldReturnForbiddenResponseWhenUserIsNotAuthorizedForEditTargetUser() throws Exception {
            Long targetUserId = 1L;
            String userEditRequest = "{}";

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userEditRequest))
                    .andExpect(status().isForbidden());
        }
    }
}