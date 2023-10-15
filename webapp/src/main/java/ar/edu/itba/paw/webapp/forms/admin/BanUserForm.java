package ar.edu.itba.paw.webapp.forms.admin;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.Size;
import java.util.UUID;

public class BanUserForm {

    @ValidUuid
    private UUID userId;
    @Size(max = 300)
    private String reason;
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}


