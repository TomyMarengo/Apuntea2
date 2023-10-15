package ar.edu.itba.paw.webapp.forms.admin;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DeleteWithReasonForm {

    @Size(max = 300)
    private String reason;

    @Pattern(regexp = "/|/notes/.*|/directory/.*")
    private String redirectUrl = "/";

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
