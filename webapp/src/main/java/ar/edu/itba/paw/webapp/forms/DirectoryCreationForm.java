package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.forms.SearchableCreationForm;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.Pattern;

public class DirectoryCreationForm extends SearchableCreationForm {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX, message = "{validation.validColors}")
    private String iconColor = "BBBBBB";

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}
