package ar.edu.itba.paw.webapp.controller.subject.dtos;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SubjectForm {
    @NotNull(message = "{error.param.empty}")
    @NotEmpty(message = "{error.param.empty}")
    @Pattern(regexp = RegexUtils.FILE_REGEX, message = "{validation.subject.name}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
