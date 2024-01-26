package ar.edu.itba.paw.webapp.controller.note.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableUpdateDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class NoteUpdateDto extends SearchableUpdateDto {

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
