package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.VerificationCode;

import java.time.LocalDateTime;
import java.util.UUID;

public interface VerificationCodeDao {
    VerificationCode saveVerificationCode(String code, User user, LocalDateTime expirationDate);
    boolean verifyForgotPasswordCode(UUID userId, String code);
    boolean deleteVerificationCodes(UUID userId);
    void removeExpiredCodes();
}
