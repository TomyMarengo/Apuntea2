package ar.edu.itba.paw.models;

import java.util.UUID;

public class Subject {
    private final UUID subjectId;
    private final String name;

    private UUID rootDirectoryId;

    Integer year;
    public Subject(UUID subjectId, String name) {
        this.subjectId = subjectId;
        this.name = name;
    }

    public Subject(UUID subjectId, String name, UUID rootDirectoryId) {
        this(subjectId, name);
        this.rootDirectoryId = rootDirectoryId;
    }

    //for subject-career relation
    public Subject(UUID subjectId, String name, Integer year) {
        this(subjectId, name);
        this.year = year;
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

    public Integer getYear() {
        return year;
    }

    @Override
    public int hashCode() {
        return subjectId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Subject)) return false;
        Subject s = (Subject) o;
        return s.subjectId.equals(subjectId);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId:" + subjectId +
                ", name:'" + name + '\'' +
                '}';
    }
}
