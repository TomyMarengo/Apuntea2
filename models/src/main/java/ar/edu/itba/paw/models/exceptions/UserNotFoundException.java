package ar.edu.itba.paw.models.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {

    }
    public UserNotFoundException(Throwable var1) {
        super(var1);
    }
}