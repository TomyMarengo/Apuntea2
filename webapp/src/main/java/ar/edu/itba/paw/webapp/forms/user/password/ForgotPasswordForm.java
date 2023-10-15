package ar.edu.itba.paw.webapp.forms.user.password;

import ar.edu.itba.paw.webapp.validation.ExistingEmail;

public class ForgotPasswordForm {
    @ExistingEmail
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
