package ar.edu.itba.paw.webapp.forms.note;

import ar.edu.itba.paw.webapp.forms.EditSearchableForm;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditNoteForm extends EditSearchableForm {

    @NotEmpty
    @Pattern(regexp = "theory|practice|exam|other")
    private String category;

    @Pattern(regexp = "/|/notes/.*|/directory/.*")
    private String redirectUrl = "/";

    private boolean visible = true;

    public String getCategory() {
        return category;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setCategory(String category) { this.category = category; }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
