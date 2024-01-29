package ar.edu.itba.paw.webapp.controller.user.dto;

import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PasswordForgotDto {
    @NotNull
    @Pattern(regexp = RegexUtils.CHALLENGE_CODE_REGEX)
    private String code;

    @NotNull
    @Size(min = 4, max = 50)
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX)
    private String password;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
