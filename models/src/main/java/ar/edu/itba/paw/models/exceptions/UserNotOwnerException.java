package ar.edu.itba.paw.models.exceptions;

public class UserNotOwnerException extends RuntimeException{
    public UserNotOwnerException() {
    }
    public UserNotOwnerException(Throwable var1) {
        super(var1);
    }
}
