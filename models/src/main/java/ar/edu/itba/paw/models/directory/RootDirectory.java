package ar.edu.itba.paw.models.directory;

import ar.edu.itba.paw.models.institutional.Subject;

import java.util.UUID;

public class RootDirectory extends Directory {
    private Subject subject;

    public RootDirectory(UUID directoryId, String name, UUID parentId, Subject subject) {
        super(directoryId, name, parentId);
        this.subject = subject;
    }

    public Subject getSubject() { return subject; }
}
