package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.VerificationCode;

import java.time.LocalDateTime;

public interface VerificationCodeDao {
    VerificationCode saveVerificationCode(String code, User user, LocalDateTime expirationDate);
    boolean verifyForgotPasswordCode(String email, String code);
    boolean deleteVerificationCodes(String email);
    void removeExpiredCodes();
}
