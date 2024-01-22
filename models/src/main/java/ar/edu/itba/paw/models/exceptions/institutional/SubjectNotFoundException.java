package ar.edu.itba.paw.models.exceptions.institutional;

import ar.edu.itba.paw.models.exceptions.NotFoundException;

public class SubjectNotFoundException extends NotFoundException {
    public SubjectNotFoundException() {
    }

    public SubjectNotFoundException(Throwable var1) {
        super(var1);
    }
}
