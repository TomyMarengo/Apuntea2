package ar.edu.itba.paw.models.exceptions.institutional;

import ar.edu.itba.paw.models.exceptions.NotFoundException;

public class CareerNotFoundException extends NotFoundException {
    public CareerNotFoundException() {
    }
    public CareerNotFoundException(Throwable var1) {
        super(var1);
    }
}
