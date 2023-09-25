package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.UnusedEmail;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UserForm {
    @NotEmpty
    @UnusedEmail
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @ValidUuid
    private UUID institutionId;

    @ValidUuid
    private UUID careerId;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getInstitutionId() {
        return institutionId;
    }
    public void setInstitutionId(UUID institutionId) {
        this.institutionId = institutionId;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

}
