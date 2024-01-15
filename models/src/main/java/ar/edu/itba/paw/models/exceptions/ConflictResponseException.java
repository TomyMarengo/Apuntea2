package ar.edu.itba.paw.models.exceptions;

public class ConflictResponseException extends RuntimeException {
    private String message;

    public ConflictResponseException(final String message) {
        super(message);
        this.message = message;
    }
}
