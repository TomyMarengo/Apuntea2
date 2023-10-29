package ar.edu.itba.paw.models.institutional.dtos;

import ar.edu.itba.paw.models.institutional.Subject;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SubjectDto implements Serializable {
    private final UUID subjectId;
    private final String name;

    public SubjectDto(Subject subject) {
        this.subjectId = subject.getSubjectId();
        this.name = subject.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectDto that = (SubjectDto) o;
        return Objects.equals(subjectId, that.subjectId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, name);
    }
}
