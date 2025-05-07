package io.github.aglushkovsky.advertisingservice.validator.group;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, DaoValidationGroup.class})
public interface DaoValidationAfterDefaultGroupSequence {
}
