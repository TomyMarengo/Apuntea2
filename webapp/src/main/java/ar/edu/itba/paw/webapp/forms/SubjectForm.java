package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

public class SubjectForm {
    @ValidUuid
    @NotBlank
    private UUID subjectId;

    @NotBlank
    @Range(min = 1, max = 10)
    private Integer year;

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
