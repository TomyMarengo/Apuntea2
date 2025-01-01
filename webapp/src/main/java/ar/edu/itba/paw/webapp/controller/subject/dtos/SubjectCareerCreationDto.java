package ar.edu.itba.paw.webapp.controller.subject.dtos;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

public class SubjectCareerCreationDto {
    @Range(min = 1, max = 5, message = "{error.param.range}")
    private int year;

    @ValidUuid
    private UUID subjectId;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }
}
