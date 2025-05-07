package io.github.aglushkovsky.advertisingservice.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Slf4j
public abstract class AbstractExistsIdInDaoValidator<I> {

    protected boolean isValid(I id, Predicate<I> isExistsPredicate, String logErrorMessage) {
        boolean result = isExistsPredicate.test(id);

        if (!result) {
            log.error(logErrorMessage, id);
        }

        return result;
    }
}
