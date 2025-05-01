package io.github.aglushkovsky.advertisingservice.exception;

public class NotFoundException extends RuntimeException {

    private static final String MESSAGE = "Could not find item with id: %d";

    public NotFoundException(Long id) {
        super(MESSAGE.formatted(id));
    }
}
