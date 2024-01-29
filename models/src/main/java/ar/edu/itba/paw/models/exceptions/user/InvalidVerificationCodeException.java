package ar.edu.itba.paw.models.exceptions.user;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException() {
    }
    public InvalidVerificationCodeException(Throwable var1) {
        super(var1);
    }
}
