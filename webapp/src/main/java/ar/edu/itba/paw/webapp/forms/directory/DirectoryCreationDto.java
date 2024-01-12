package ar.edu.itba.paw.webapp.forms.directory;

import ar.edu.itba.paw.webapp.forms.SearchableCreationDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.Pattern;

public class DirectoryCreationDto extends SearchableCreationDto {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX)

    private String color = "BBBBBB";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
