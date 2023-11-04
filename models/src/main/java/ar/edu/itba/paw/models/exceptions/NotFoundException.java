package ar.edu.itba.paw.models.exceptions;

public abstract class NotFoundException extends RuntimeException{
    public NotFoundException() {
    }
    public NotFoundException(Throwable var1) {
        super(var1);
    }
}
