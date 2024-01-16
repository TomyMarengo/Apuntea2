package ar.edu.itba.paw.models.exceptions.institutional;

import ar.edu.itba.paw.models.exceptions.NotFoundException;

public class InstitutionNotFoundException extends NotFoundException {
    public InstitutionNotFoundException() {
    }

    public InstitutionNotFoundException(Throwable var1) {
        super(var1);
    }
}
