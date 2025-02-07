package ar.edu.itba.paw.webapp.auth.exceptions;

import ar.edu.itba.paw.webapp.exceptions.ApiHttpError;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class InvalidJwtTokenException extends AuthenticationException implements ApiHttpError {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private final String details;

    public InvalidJwtTokenException(String s, Throwable t) {
        super(s, t);
        this.details = s;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }

    @Override
    public String getDetails() {
        return details;
    }
}
