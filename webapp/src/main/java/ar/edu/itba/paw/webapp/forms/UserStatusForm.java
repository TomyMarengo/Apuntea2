package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.models.user.UserStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserStatusForm {
    @NotNull(message = "{error.param.empty}")
    @Pattern(regexp = "^(active|banned)$")
    private String status;

    @Size(max = 255, message = "{error.param.length}")
    private String reason;

    public UserStatusForm() {
    }

    public String getStatus() {
        return status;
    }

    public UserStatus getUserStatus() {
        return UserStatus.fromString(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
