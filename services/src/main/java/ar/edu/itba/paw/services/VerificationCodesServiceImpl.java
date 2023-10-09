package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.VerificationCode;
import ar.edu.itba.paw.models.exceptions.InvalidVerificationCodeException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.VerificationCodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@Service
public class VerificationCodesServiceImpl implements VerificationCodesService  {
    private EmailService emailService;
    private VerificationCodeDao verificationCodeDao;

    @Autowired
    public VerificationCodesServiceImpl(EmailService emailService, VerificationCodeDao verificationCodeDao) {
        this.emailService = emailService;
        this.verificationCodeDao = verificationCodeDao;
    }

    @Override
    @Transactional
    public void sendForgotPasswordCode(String email) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        VerificationCode verificationCode = new VerificationCode(String.format("%06d", number), email, LocalDateTime.now().plusMinutes(10));
        if (!verificationCodeDao.saveVerificationCode(verificationCode))
            throw new InvalidVerificationCodeException();

        final Locale lang = LocaleContextHolder.getLocale();
        emailService.sendForgotPasswordEmail(verificationCode, lang);
    }

    @Override
    @Transactional
    public boolean verifyForgotPasswordCode(String email, String code) {
        return verificationCodeDao.verifyForgotPasswordCode(email, code) &&
                verificationCodeDao.deleteVerificationCodes(email);
    }
}
