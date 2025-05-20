package io.github.aglushkovsky.advertisingservice.exception;

public class AddUserRateToYourselfException extends RuntimeException {

    private static final String MESSAGE = "An attempt to add a rating to yourself";

    public AddUserRateToYourselfException() {
        super(MESSAGE);
    }
}
