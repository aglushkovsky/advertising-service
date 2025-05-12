package io.github.aglushkovsky.advertisingservice.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

    private static final String MESSAGE = "Could not find item with id: %d";

    @Getter
    private final Long id;

    public NotFoundException(Long id) {
        super(MESSAGE.formatted(id));
        this.id = id;
    }
}
