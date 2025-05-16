package io.github.aglushkovsky.advertisingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aglushkovsky.advertisingservice.annotation.WebMvcUnitTest;
import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.aglushkovsky.advertisingservice.util.UserTestUtils.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcUnitTest(UserController.class)
class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @WithAnonymousUser
    class CreateUser {

        @Test
        void createUserShouldCreateUserWhenAllParametersAreValidAndUserIsNotAuthorized() throws Exception {
            UserCreateEditRequestDto userCreateEditRequestDtoStub = createUserCreateEditRequestDtoStub();
            UserResponseDto userResponseDtoStub = createUserResponseDtoStub();
            doReturn(userResponseDtoStub).when(userService).createUser(userCreateEditRequestDtoStub);

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDtoStub)))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(authorities = "USER")
        void createUserShouldReturnForbiddenResponseWhenAllParametersAreValidAndUserIsAuthorized() throws Exception {
            UserCreateEditRequestDto userCreateEditRequestDtoStub = createUserCreateEditRequestDtoStub();

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDtoStub)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
        }

        @Test
        void createUserShouldReturnBadRequestResponseWhenAllParametersAreFilledButInvalid() throws Exception {
            UserCreateEditRequestDto userCreateEditRequestDto = UserCreateEditRequestDto.builder()
                    .login("s")
                    .password("s")
                    .email("s")
                    .phoneNumber("s")
                    .build();
            int countOfErrors = 5;

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(countOfErrors));
        }

        @Test
        void createUserShouldReturnBadRequestResponseWhenRequiredParametersAreNull() throws Exception {
            UserCreateEditRequestDto userCreateEditRequestDto = UserCreateEditRequestDto.builder()
                    .login(null)
                    .password(null)
                    .build();
            int countOfErrors = 2;

            mockMvc.perform(post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(countOfErrors));
        }
    }

    @Nested
    class EditUser {

        @Test
        void editUserShouldEditUserWhenTargetUserIdAndAllEditRequestAreValid() throws Exception {
            Long targetUserId = 1L;
            UserCreateEditRequestDto userCreateEditRequestDto = UserCreateEditRequestDto.builder()
                    .login("test_login")
                    .password("qwerty12345")
                    .phoneNumber("+79201234567")
                    .email("example@example.com")
                    .build();
            UserResponseDto userResponseDtoStub = UserResponseDto.builder()
                    .id(targetUserId)
                    .login(userCreateEditRequestDto.login())
                    .email(userCreateEditRequestDto.email())
                    .phoneNumber(userCreateEditRequestDto.phoneNumber())
                    .build();
            doReturn(userResponseDtoStub).when(userService).editUser(targetUserId, userCreateEditRequestDto);

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(targetUserId))
                    .andExpect(jsonPath("$.login").value(userCreateEditRequestDto.login()))
                    .andExpect(jsonPath("$.email").value(userCreateEditRequestDto.email()))
                    .andExpect(jsonPath("$.phoneNumber").value(userCreateEditRequestDto.phoneNumber()));
        }

        @Test
        void editUserShouldReturnBadRequestWhenTargetUserIdIsInvalidButEditRequestIsValid() throws Exception {
            Long targetUserId = 0L;
            UserCreateEditRequestDto userCreateEditRequestDto = UserCreateEditRequestDto.builder()
                    .login("test_login")
                    .password("qwerty12345")
                    .phoneNumber("+79201234567")
                    .email("example@example.com")
                    .build();

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }

        @Test
        void editUserShouldReturnBadRequestWhenTargetUserIdIsValidButEditRequestIsInvalid() throws Exception {
            Long targetUserId = 1L;
            UserCreateEditRequestDto userCreateEditRequestDto = UserCreateEditRequestDto.builder()
                    .login("test")
                    .password("qwerty")
                    .phoneNumber("9201234567")
                    .email("example")
                    .build();
            int countOfErrors = 4;

            mockMvc.perform(patch("/api/v1/users/{0}", targetUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateEditRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.errors.size()").value(countOfErrors));
        }
    }

    @Nested
    class GetUserById {

        @Test
        void getUserByIdShouldReturnUserWhenUserIdIsValid() throws Exception {
            Long userId = 1L;
            UserResponseDto userResponseDtoStub = createUserResponseDtoStub();
            doReturn(userResponseDtoStub).when(userService).findById(userId);

            mockMvc.perform(get("/api/v1/users/{0}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(userResponseDtoStub)));
        }

        @Test
        void getUserByIdShouldReturnUserWhenUserIdIsInvalid() throws Exception {
            Long userId = 0L;

            mockMvc.perform(get("/api/v1/users/{0}", userId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.size()").value(1));
        }
    }
}