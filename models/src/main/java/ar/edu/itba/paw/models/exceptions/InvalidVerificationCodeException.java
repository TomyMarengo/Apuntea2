package ar.edu.itba.paw.models.exceptions;

public class InvalidVerificationCodeException extends RuntimeException{
    public InvalidVerificationCodeException() {
    }
    public InvalidVerificationCodeException(Throwable var1) {
        super(var1);
    }
}
