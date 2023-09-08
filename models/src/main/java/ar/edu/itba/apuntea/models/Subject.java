package ar.edu.itba.apuntea.models;

import java.util.UUID;

public class Subject {
    private UUID subjectId;
    private String name;
    public Subject(UUID subjectId, String name) {
        this.subjectId = subjectId;
        this.name = name;
    }
    public UUID getSubjectId() {
        return subjectId;
    }
    public String getName() {
        return name;
    }
}
