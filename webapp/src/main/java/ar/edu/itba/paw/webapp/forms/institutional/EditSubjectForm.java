package ar.edu.itba.paw.webapp.forms.institutional;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

public class EditSubjectForm {
    @ValidUuid
    @NotNull
    private UUID subjectId;


    @Range(min = 1, max = 10)
    private int year;

    @NotBlank
    @Pattern(regexp = RegexUtils.FILE_REGEX)
    private String name;

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	this.name = name;
    }
}
