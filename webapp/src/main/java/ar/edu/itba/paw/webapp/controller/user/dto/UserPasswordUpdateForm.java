package ar.edu.itba.paw.webapp.controller.user.dto;

import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserPasswordUpdateForm {
    @NotNull(message = "{error.param.empty}")
    @Size(min = 4, max = 50, message = "{error.param.length}")
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX, message = "{error.password.invalid}")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
