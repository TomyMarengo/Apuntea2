package ar.edu.itba.paw.webapp.controller.user.dto;

import ar.edu.itba.paw.models.user.UserStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserStatusDto {
    @NotNull
    @Pattern(regexp = "^(active|banned)$")
    private String status;

    @Size(max = 255)
    private String reason;

    public UserStatusDto() {
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
