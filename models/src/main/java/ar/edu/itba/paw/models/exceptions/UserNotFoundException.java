package ar.edu.itba.paw.models.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {

    }

    public UserNotFoundException(final String message) {
        super(message);
    }
}