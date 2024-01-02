package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;

public interface ApiHttpError {
//    ApiErrorCode getApiCode();
    HttpStatus getHttpStatus();
    String getDetails();
}
