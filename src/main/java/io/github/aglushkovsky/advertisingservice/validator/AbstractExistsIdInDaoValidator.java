package io.github.aglushkovsky.advertisingservice.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Slf4j
public abstract class AbstractExistsIdInDaoValidator<I> {

    protected boolean isValid(I id, Predicate<I> isExistsPredicate, String logErrorMessage) {
        log.info("Checking if id {} exists", id);

        boolean result = isExistsPredicate.test(id);

        if (!result) {
            log.error(logErrorMessage, id);
        } else {
            log.info("Finished isValid successfully; id={}", id);
        }

        return result;
    }
}
