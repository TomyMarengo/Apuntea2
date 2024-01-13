package ar.edu.itba.paw.webapp.controller.directory.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableCreationDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.Pattern;

public class DirectoryCreationDto extends SearchableCreationDto {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX)

    private String iconColor = "BBBBBB";

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}
