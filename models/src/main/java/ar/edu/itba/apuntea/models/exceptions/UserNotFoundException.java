package ar.edu.itba.apuntea.models.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {

    }

    public UserNotFoundException(final String message) {
        super(message);
    }
}