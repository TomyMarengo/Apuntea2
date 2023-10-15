package ar.edu.itba.paw.webapp.forms.admin;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import java.util.UUID;

public class UnbanUserForm {

    @ValidUuid
    private UUID userId;
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
