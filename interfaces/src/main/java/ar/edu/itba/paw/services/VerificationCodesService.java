package ar.edu.itba.paw.services;

import java.util.UUID;

public interface VerificationCodesService {
    void sendForgotPasswordCode(UUID id);
    boolean verifyForgotPasswordCode(UUID userId, String code);
}
