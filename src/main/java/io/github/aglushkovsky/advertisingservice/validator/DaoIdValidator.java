package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class DaoIdValidator {

    public static void validateId(Long id,
                                  Function<Long, Boolean> isExistsFunction,
                                  String logErrorMessage,
                                  boolean isOptional) {
        Optional.ofNullable(id)
                .map(isExistsFunction)
                .ifPresentOrElse(
                        isExists -> {
                            if (!isExists) {
                                throwNotFoundException(id, logErrorMessage);
                            }
                        },
                        () -> {
                            if (!isOptional) {
                                throwNotFoundException(id, logErrorMessage);
                            }
                        }
                );
    }

    private static void throwNotFoundException(Long id, String logErrorMessage) {
        log.error(logErrorMessage, id);
        throw new NotFoundException(id);
    }
}
