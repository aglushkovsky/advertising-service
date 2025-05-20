package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.UserMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.util.SecurityTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.UserTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUser {

        @Test
        void createUserShouldCreateUserWhenAllParametersAreValid() {
            User createdUserStub = createUserStub(null);
            User savedUserStub = createUserStub(1L);
            UserResponseDto userResponseDtoStub = createUserResponseDtoStub();
            doReturn(createdUserStub).when(userMapper).toUser(createUserCreateEditRequestDtoStub());
            doReturn(savedUserStub).when(userDao).add(createdUserStub);
            doReturn(userResponseDtoStub).when(userMapper).toDto(savedUserStub);

            UserResponseDto result = userService.createUser(createUserCreateEditRequestDtoStub());

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(userResponseDtoStub);
            verify(userMapper).toUser(createUserCreateEditRequestDtoStub());
            verify(userDao).add(createdUserStub);
            verify(userMapper).toDto(savedUserStub);
            verifyNoMoreInteractions(userMapper);
            verifyNoMoreInteractions(userDao);
        }
    }

    @Nested
    class EditUser {

        @Test
        void editUserShouldEditUserWhenAllParametersAreValid() {
            Long userId = 1L;
            setMockUserInSecurityContext(userId);
            User userStub = createUserStub(userId);
            doReturn(Optional.of(userStub)).when(userDao).findById(userId);
            User updatedUserStub = mock(User.class);
            doReturn(updatedUserStub).when(userMapper).updateUser(userStub, createUserCreateEditRequestDtoStub());
            UserResponseDto updatedUserResponseDtoStub = mock(UserResponseDto.class);
            doReturn(updatedUserResponseDtoStub).when(userMapper).toDto(updatedUserStub);

            UserResponseDto result = userService.editUser(userId, createUserCreateEditRequestDtoStub());

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(updatedUserResponseDtoStub);
            verify(userDao).findById(userId);
            verify(userMapper).updateUser(userStub, createUserCreateEditRequestDtoStub());
            verify(userMapper).toDto(updatedUserStub);
            verify(userDao).update(updatedUserStub);
            verifyNoMoreInteractions(userMapper);
            verifyNoMoreInteractions(userDao);
        }

        @Test
        void editUserShouldThrowExceptionWhenUserIsNotAuthorizedForEdit() {
            Long authorizedUserId = 1L;
            Long targetUserId = 2L;
            setMockUserInSecurityContext(authorizedUserId);
            doReturn(Optional.of(mock(User.class))).when(userDao).findById(targetUserId);

            assertThatThrownBy(() -> userService.editUser(targetUserId, createUserCreateEditRequestDtoStub()))
                    .isInstanceOf(AccessDeniedException.class);
            verify(userDao).findById(targetUserId);
        }

        @Test
        void editUserShouldThrowExceptionWhenTargetUserIdDoesNotExist() {
            Long authorizedUserId = 1L;
            Long targetUserId = 100L;
            setMockUserInSecurityContext(authorizedUserId);
            doReturn(Optional.empty()).when(userDao).findById(targetUserId);

            assertThatThrownBy(() -> userService.editUser(targetUserId, createUserCreateEditRequestDtoStub()))
                    .isInstanceOf(NotFoundException.class);
            verify(userDao).findById(targetUserId);
        }
    }

    @Nested
    class FindById {

        @Test
        void findByIdShouldReturnUserWhenUserExists() {
            Long userId = 1L;
            User userMock = mock(User.class);
            doReturn(Optional.of(userMock)).when(userDao).findById(userId);
            UserResponseDto userResponseDtoStub = mock(UserResponseDto.class);
            doReturn(userResponseDtoStub).when(userMapper).toDto(userMock);

            UserResponseDto result = userService.findById(userId);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(userResponseDtoStub);
            verify(userDao).findById(userId);
            verify(userMapper).toDto(userMock);
        }

        @Test
        void findByIdShouldThrowExceptionWhenUserDoesNotExist() {
            Long userId = 1L;
            doReturn(Optional.empty()).when(userDao).findById(userId);

            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(NotFoundException.class);
            verify(userDao).findById(userId);
        }
    }
}