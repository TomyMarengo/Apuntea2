package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import ar.edu.itba.paw.webapp.validation.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@AttributeDependence(baseField = "password", dependentField = "oldPassword")
public class UserUpdateForm {
    @Pattern(regexp = RegexUtils.NAME_REGEX, message = "{validation.user.name}")
    @Size(max = 20, message = "{error.param.length}")
    private String firstName;

    @Pattern(regexp = RegexUtils.NAME_REGEX, message = "{validation.user.name}")
    @Size(max = 20, message = "{error.param.length}")
    private String lastName;

    @UnusedUsername
    @Size(max = 30, message = "{error.param.length}")
    @Pattern(regexp = RegexUtils.USERNAME_REGEX, message = "{validation.user.username}")
    private String username;

    private UUID careerId;

    @Size(min = 4, max = 50, message = "{error.param.length}")
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX, message = "{error.password.invalid}")
    private String password;


    @Size(min = 4, max = 50, message = "{error.param.length}")
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX, message = "{error.password.invalid}")
    @MatchesCurrentUserPassword
    private String oldPassword;

    private Boolean notificationsEnabled;

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
