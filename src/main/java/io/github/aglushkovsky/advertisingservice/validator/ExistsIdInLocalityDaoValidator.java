package io.github.aglushkovsky.advertisingservice.validator;

import io.github.aglushkovsky.advertisingservice.dao.impl.LocalityDao;
import io.github.aglushkovsky.advertisingservice.validator.annotation.ExistsIdInLocalityDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExistsIdInLocalityDaoValidator extends AbstractExistsIdInDaoValidator<Long>
        implements ConstraintValidator<ExistsIdInLocalityDao, Long> {

    private final LocalityDao localityDao;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value == null || isValid(value, localityDao::isExists, "Not found locality with id {}");
    }
}
