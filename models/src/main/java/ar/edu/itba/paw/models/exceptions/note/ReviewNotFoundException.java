package ar.edu.itba.paw.models.exceptions.note;

import ar.edu.itba.paw.models.exceptions.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException() {
    }

    public ReviewNotFoundException(Throwable var1) {
        super(var1);
    }
}
