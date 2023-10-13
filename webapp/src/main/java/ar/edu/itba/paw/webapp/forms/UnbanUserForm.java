package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.Size;
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
