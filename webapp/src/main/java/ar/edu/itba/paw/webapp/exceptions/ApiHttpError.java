package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;

public interface ApiHttpError {
    HttpStatus getHttpStatus();
    String getDetails();
}
