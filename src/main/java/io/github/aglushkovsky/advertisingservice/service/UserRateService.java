package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserRateDao;
import io.github.aglushkovsky.advertisingservice.dto.request.UserRateCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.UserRateResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.UserRate;
import io.github.aglushkovsky.advertisingservice.exception.AddUserRateToYourselfException;
import io.github.aglushkovsky.advertisingservice.exception.UserRateAlreadyExistsException;
import io.github.aglushkovsky.advertisingservice.mapper.UserRateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static io.github.aglushkovsky.advertisingservice.util.SecurityUtils.*;
import static io.github.aglushkovsky.advertisingservice.validator.DaoIdValidator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRateService {

    private final UserRateDao userRateDao;
    private final UserRateMapper userRateMapper;
    private final UserDao userDao;

    public UserRateResponseDto createUserRate(Long recipientId, UserRateCreateRequestDto userRateCreateRequestDto) {
        log.info("Creating rate for recipientId={}; userRateCreateRequestDto: {}", recipientId, userRateCreateRequestDto);

        checkIsAuthenticatedUserCanLeftUserRate(recipientId);

        UserRate createdUserRate = userRateMapper.toEntity(recipientId, userRateCreateRequestDto);
        UserRate savedCreatedUserRate = userRateDao.add(createdUserRate);
        UserRateResponseDto userRateResponseDto = userRateMapper.toDto(savedCreatedUserRate);
        log.info("Created rate for recipientId={} with id={}", recipientId, userRateResponseDto.id());
        recalculateRecipientUserTotalRating(recipientId);

        return userRateResponseDto;
    }

    private void checkIsAuthenticatedUserCanLeftUserRate(Long recipientId) {
        Long authenticatedUserId = getAuthenticatedUserId();

        if (authenticatedUserId.equals(recipientId)) {
            log.error("An attempt to add a rating to yourself");
            throw new AddUserRateToYourselfException();
        }

        validateId(recipientId, userDao::isExists, "Not found recipient with id={}", false);

        userRateDao.findByAuthorAndRecipientId(authenticatedUserId, recipientId)
                .ifPresent(result -> {
                    log.error("Authenticated user (id={}) has already left a rating for the user with id={}",
                            authenticatedUserId, recipientId);
                    throw new UserRateAlreadyExistsException();
                });
    }

    private void recalculateRecipientUserTotalRating(Long recipientId) {
        double recalculatedUserTotalRating = userRateDao
                .findByRecipientId(recipientId)
                .stream()
                .mapToDouble(UserRate::getValue)
                .average()
                .orElseThrow();

        double roundedRecalculatedTotalRating = Math.round(recalculatedUserTotalRating * 10.0) / 10.0;
        userDao.updateTotalRating(recipientId, roundedRecalculatedTotalRating);
        log.info("Total rating for user with id={} was recalculated; new value: {}",
                recipientId, roundedRecalculatedTotalRating);
    }
}
