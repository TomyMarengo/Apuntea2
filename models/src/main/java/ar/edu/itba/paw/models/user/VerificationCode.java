package ar.edu.itba.paw.models.user;

import java.time.LocalDateTime;

public class VerificationCode {
    private final String code;
    private final String email;
    private final LocalDateTime expirationDate;

    public VerificationCode(String code, String email, LocalDateTime expirationDate) {
        this.code = code;
        this.email = email;
        this.expirationDate = expirationDate;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
