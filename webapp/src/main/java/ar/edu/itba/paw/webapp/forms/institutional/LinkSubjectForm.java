package ar.edu.itba.paw.webapp.forms.institutional;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class LinkSubjectForm {
    @ValidUuid
    @NotNull
    private UUID subjectId;

    @NotNull
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
