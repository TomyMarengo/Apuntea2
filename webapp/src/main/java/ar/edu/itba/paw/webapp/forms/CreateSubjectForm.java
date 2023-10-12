package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

public class CreateSubjectForm {
    @Range(min = 1, max = 10)
    private int year;

    @NotBlank
    @Pattern(regexp = "^(?!([ ,\\-_]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ \\-_,]+$")
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
