package ar.edu.itba.paw.webapp.forms.user.password;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChallengeForm {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Pattern(regexp = RegexUtils.CHALLENGE_CODE_REGEX)
    private String code;

    @NotNull
    @Size(min = 4, max = 50)
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX)
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
