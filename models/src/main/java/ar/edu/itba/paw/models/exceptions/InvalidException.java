package ar.edu.itba.paw.models.exceptions;

public abstract class InvalidException extends RuntimeException {
    public InvalidException() {
    }

    public InvalidException(String var1) {
        super(var1);
    }

    public InvalidException(Throwable var1) {
        super(var1);
    }
}
