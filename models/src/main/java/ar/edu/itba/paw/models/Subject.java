package ar.edu.itba.paw.models;

import java.util.UUID;

public class Subject {
    private final UUID subjectId;
    private final String name;

    private UUID rootDirectoryId;

    public Subject(UUID subjectId, String name) {
        this.subjectId = subjectId;
        this.name = name;
    }

    public Subject(UUID subjectId, String name, UUID rootDirectoryId) {
        this(subjectId, name);
        this.rootDirectoryId = rootDirectoryId;
    }
    public UUID getSubjectId() {
        return subjectId;
    }
    public String getName() {
        return name;
    }

    public UUID getRootDirectoryId() {
        return rootDirectoryId;
    }
}
