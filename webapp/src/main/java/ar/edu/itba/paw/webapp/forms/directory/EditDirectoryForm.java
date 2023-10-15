package ar.edu.itba.paw.webapp.forms.directory;

import ar.edu.itba.paw.webapp.forms.EditSearchableForm;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditDirectoryForm extends EditSearchableForm {

    private boolean visible = true;

    @Pattern(regexp = "(BBBBBB|16A765|4986E7|CD35A6)") //TODO: Tener en un solo lado los colores
    private String color;

    @Pattern(regexp = "/|/notes/.*|/directory/.*")
    private String redirectUrl = "/";

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
