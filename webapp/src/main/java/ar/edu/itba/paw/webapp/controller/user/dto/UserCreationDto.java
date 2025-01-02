package ar.edu.itba.paw.webapp.controller.user.dto;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import ar.edu.itba.paw.webapp.validation.UnusedEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class UserCreationDto {

    @NotEmpty(message = "{error.param.empty}")
    @Email(message = "{error.email.invalid}")
    @UnusedEmail
    private String email;

    @NotNull(message = "{error.param.empty}")
    @Size(min = 4, max = 50, message = "{error.param.length}")
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX, message = "{error.password.invalid}")
    private String password;

    @NotNull(message = "{error.param.empty}")
    private UUID careerId;

    public UserCreationDto() {
    }

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

    public UUID getCareerId() {
        return careerId;
    }

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

}
