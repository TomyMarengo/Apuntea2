package ar.edu.itba.paw.webapp.forms.directory;

import ar.edu.itba.paw.webapp.forms.EditSearchableForm;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditDirectoryForm extends EditSearchableForm {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX)
    private String color;

    @Pattern(regexp = RegexUtils.SEARCHABLE_REDIRECT)
    private String redirectUrl = "/";

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
