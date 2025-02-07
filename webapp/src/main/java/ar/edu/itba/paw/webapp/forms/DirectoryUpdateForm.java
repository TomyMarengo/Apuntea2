package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.forms.SearchableUpdateDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.Pattern;

public class DirectoryUpdateForm extends SearchableUpdateDto {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX, message = "{validation.validColors}")
    private String iconColor;

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}
