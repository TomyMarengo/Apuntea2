package ar.edu.itba.paw.webapp.forms.user;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import ar.edu.itba.paw.webapp.validation.UnusedEmail;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class UserForm {

    @NotEmpty
    @Email
    @UnusedEmail
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50)
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX)
    private String password;

    @NotNull
    @ValidUuid
    private UUID institutionId;

    @NotNull
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
