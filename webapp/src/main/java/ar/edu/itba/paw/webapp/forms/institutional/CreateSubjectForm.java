package ar.edu.itba.paw.webapp.forms.institutional;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

public class CreateSubjectForm {
    @Range(min = 1, max = 10)
    private int year = 1;

    @NotBlank
    @Pattern(regexp = RegexUtils.FILE_REGEX)
    private String name;

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
