package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.UserCreateEditRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import io.github.aglushkovsky.advertisingservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    private final UserMapper userMapper;

    public UserResponseDto createUser(UserCreateEditRequestDto userCreateEditRequestDto) {
        log.info("Start createUser");

        User user = userMapper.toUser(userCreateEditRequestDto);
        User createdUser = userDao.add(user);
        UserResponseDto userResponseDto = userMapper.toDto(createdUser);

        log.info("End createUser; created user with id={}", createdUser.getId());

        return userResponseDto;
    }

    public UserResponseDto editUser(Long id, UserCreateEditRequestDto userCreateEditRequestDto) {
        log.info("Start editUser");

        if (!isAuthorizedForEditUser(id)) {
            log.error("User is not authorized for editing user with id={}", id);
            throw new AccessDeniedException("User is not authorized to edit user with id=%d".formatted(id));
        }

        User userForEdit = userDao.findById(id).orElseThrow(() -> {
            log.error("User with id={} not found", id);
            return new NotFoundException(id);
        });
        User updatedUser = userMapper.updateUser(userForEdit, userCreateEditRequestDto);
        userDao.update(updatedUser);
        UserResponseDto userResponseDto = userMapper.toDto(updatedUser);

        log.info("Finished editUser successfully; edited user with id={}", id);

        return userResponseDto;
    }

    private boolean isAuthorizedForEditUser(Long targetUserEditId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(Role.ADMIN) ||
               Objects.equals(authentication.getId(), targetUserEditId);
    }
}
