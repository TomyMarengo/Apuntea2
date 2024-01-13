package ar.edu.itba.paw.webapp.controller.directory.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableUpdateDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.Pattern;

public class DirectoryUpdateDto extends SearchableUpdateDto {
    @Pattern(regexp = RegexUtils.AVAILABLE_FOLDER_COLORS_REGEX)

    private String iconColor;

    @Pattern(regexp = RegexUtils.SEARCHABLE_REDIRECT)
    private String redirectUrl = "/";

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}
