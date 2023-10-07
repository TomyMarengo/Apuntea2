package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.MatchesCurrentUserPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChangePasswordForm {

    @NotNull
    @MatchesCurrentUserPassword
    private String oldPassword;

    @NotNull
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
