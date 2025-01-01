package ar.edu.itba.paw.webapp.controller.note.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableUpdateDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class NoteUpdateDto extends SearchableUpdateDto {
    @Pattern(regexp = RegexUtils.CATEGORY_REGEX, message = "{validation.category}")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) { this.category = category; }
}
