package ar.edu.itba.paw.services;

public interface VerificationCodesService {
    void sendForgotPasswordCode(String email);
    boolean verifyForgotPasswordCode(String email, String code);
}
