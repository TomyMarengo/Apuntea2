package ar.edu.itba.paw.webapp.controller.subject.dtos;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SubjectDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp = RegexUtils.FILE_REGEX)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
