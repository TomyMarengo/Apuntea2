package ar.edu.itba.paw.models.exceptions.directory;

import ar.edu.itba.paw.models.exceptions.NotFoundException;

public class DirectoryNotFoundException extends NotFoundException {
    public DirectoryNotFoundException() {
    }
    public DirectoryNotFoundException(Throwable var1) {
        super(var1);
    }
}
