package ar.edu.itba.paw.models.exceptions.user;

import ar.edu.itba.paw.models.exceptions.InvalidException;

public class InvalidUserFollowException extends InvalidException {
    public InvalidUserFollowException() {
        super("error.follow.invalid");
    }
}
