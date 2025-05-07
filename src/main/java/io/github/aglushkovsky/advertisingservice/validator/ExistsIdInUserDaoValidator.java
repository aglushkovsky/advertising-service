package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ExistsIdInUserDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExistsIdInUserDaoValidator extends AbstractExistsIdInDaoValidator<Long>
        implements ConstraintValidator<ExistsIdInUserDao, Long> {

    private final UserDao userDao;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value == null || isValid(value, userDao::isExists, "Not found publisher with id {}");
    }
}
