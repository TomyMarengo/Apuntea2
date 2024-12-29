package ar.edu.itba.paw.webapp.controller.user.dto;

import org.hibernate.validator.constraints.Email;

public class RequestPasswordChangeDto {
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
