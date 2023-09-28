package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class EditDirectoryForm {
    @NotEmpty
    @Size(min = 2, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String name;

    @Pattern(regexp = "/|/notes/.*|/directory/.*")
    private String redirectUrl = "/";

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
