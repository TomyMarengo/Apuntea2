package ar.edu.itba.paw.webapp.forms.note;

import ar.edu.itba.paw.webapp.forms.EditSearchableForm;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class EditNoteForm extends EditSearchableForm {

    @NotEmpty
    @Pattern(regexp = RegexUtils.CATEGORY_REGEX)
    private String category;

    @Pattern(regexp = RegexUtils.SEARCHABLE_REDIRECT)
    private String redirectUrl = "/";

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

}
