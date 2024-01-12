package ar.edu.itba.paw.webapp.forms.directory;

import ar.edu.itba.paw.webapp.forms.SearchableUpdateDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.Pattern;

public class DirectoryUpdateDto extends SearchableUpdateDto {
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
