package io.github.aglushkovsky.advertisingservice.exception;

public class UserRateAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "The authenticated user has already left a rating for the recipient";

    public UserRateAlreadyExistsException() {
        super(MESSAGE);
    }
}
