package ar.edu.itba.paw.models.exceptions.note;

import ar.edu.itba.paw.models.exceptions.NotFoundException;

public class NoteNotFoundException extends NotFoundException {
    public NoteNotFoundException() {
    }
    public NoteNotFoundException(Throwable var1) {
        super(var1);
    }
}
