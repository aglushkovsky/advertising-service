package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserRateDao;
import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import io.github.aglushkovsky.advertisingservice.exception.AddUserRateToYourselfException;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.exception.UserRateAlreadyExistsException;
import io.github.aglushkovsky.advertisingservice.mapper.UserRateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.util.SecurityTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.UserRateServiceTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRateServiceTest {

    @Mock
    private UserRateDao userRateDao;

    @Mock
    private UserRateMapper userRateMapper;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserRateService userRateService;

    @Test
    void createUserRateShouldCreateUserRateWhenAllParametersAreValidAndUserIsAuthorizedForThisAction() {
        Long recipientId = 1L;
        Long authorId = 2L;
        UserRateCreateRequestDto userRateCreateRequestDto = createUserRateCreateRequestDtoStub();
        UserRate createdUserRateStub = createUserRateStub(null);
        UserRate savedUserRateStub = createUserRateStub(1L);
        UserRateResponseDto savedUserRateResponseDtoStub = UserRateResponseDto.builder()
                .id(savedUserRateStub.getId())
                .createdAt(savedUserRateStub.getCreatedAt())
                .value(savedUserRateStub.getValue())
                .build();
        setMockUserInSecurityContext(authorId);
        doReturn(true).when(userDao).isExists(recipientId);
        doReturn(Optional.empty()).when(userRateDao).findByAuthorAndRecipientId(authorId, recipientId);
        doReturn(createdUserRateStub).when(userRateMapper).toEntity(recipientId, userRateCreateRequestDto);
        doReturn(savedUserRateStub).when(userRateDao).add(createdUserRateStub);
        doReturn(savedUserRateResponseDtoStub).when(userRateMapper).toDto(savedUserRateStub);
        doReturn(List.of(createdUserRateStub)).when(userRateDao).findByRecipientId(recipientId);

        UserRateResponseDto result = userRateService.createUserRate(recipientId, userRateCreateRequestDto);

        assertThat(result)
                .isNotNull()
                .isEqualTo(savedUserRateResponseDtoStub);
        verify(userDao).isExists(recipientId);
        verify(userRateDao).findByAuthorAndRecipientId(authorId, recipientId);
        verify(userRateDao).add(createdUserRateStub);
        verify(userRateMapper).toDto(savedUserRateStub);
        verify(userDao).updateTotalRating(eq(recipientId), anyDouble());
    }

    @Test
    void createUserRateShouldThrowExceptionWhenUserAttemptsRateYourself() {
        Long recipientId = 1L;
        Long authorId = 1L;
        UserRateCreateRequestDto userRateCreateRequestDto = createUserRateCreateRequestDtoStub();
        setMockUserInSecurityContext(authorId);

        assertThatThrownBy(() -> userRateService.createUserRate(recipientId, userRateCreateRequestDto))
                .isInstanceOf(AddUserRateToYourselfException.class);
    }

    @Test
    void createUserRateShouldThrowExceptionWhenRecipientIdDoesNotExist() {
        Long recipientId = 1L;
        Long authorId = 2L;
        UserRateCreateRequestDto userRateCreateRequestDto = createUserRateCreateRequestDtoStub();
        setMockUserInSecurityContext(authorId);
        doReturn(false).when(userDao).isExists(recipientId);

        assertThatThrownBy(() -> userRateService.createUserRate(recipientId, userRateCreateRequestDto))
                .isInstanceOf(NotFoundException.class);
        verify(userDao).isExists(recipientId);
    }

    @Test
    void createUserRateShouldThrowExceptionWhenUserRateAlreadyExists() {
        Long recipientId = 1L;
        Long authorId = 2L;
        UserRateCreateRequestDto userRateCreateRequestDto = createUserRateCreateRequestDtoStub();
        setMockUserInSecurityContext(authorId);
        doReturn(true).when(userDao).isExists(recipientId);
        doReturn(Optional.of(mock(UserRate.class))).when(userRateDao).findByAuthorAndRecipientId(authorId, recipientId);

        assertThatThrownBy(() -> userRateService.createUserRate(recipientId, userRateCreateRequestDto))
                .isInstanceOf(UserRateAlreadyExistsException.class);
        verify(userDao).isExists(recipientId);
        verify(userRateDao).findByAuthorAndRecipientId(authorId, recipientId);
    }
}