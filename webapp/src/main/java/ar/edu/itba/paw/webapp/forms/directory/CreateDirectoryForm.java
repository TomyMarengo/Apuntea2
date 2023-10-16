package ar.edu.itba.paw.webapp.forms.directory;

import ar.edu.itba.paw.webapp.forms.CreateSearchableForm;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateDirectoryForm extends CreateSearchableForm {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX)

    private String color = "BBBBBB";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
