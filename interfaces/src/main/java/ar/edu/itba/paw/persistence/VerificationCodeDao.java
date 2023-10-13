package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.VerificationCode;

public interface VerificationCodeDao {
    void saveVerificationCode(VerificationCode verificationCode);
    boolean verifyForgotPasswordCode(String email, String code);
    boolean deleteVerificationCodes(String email);
    void removeExpiredCodes();
}
