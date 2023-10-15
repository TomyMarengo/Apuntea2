package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.user.VerificationCode;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationCodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@Service
public class VerificationCodesServiceImpl implements VerificationCodesService  {
    private final EmailService emailService;
    private final VerificationCodeDao verificationCodeDao;
    private final UserDao userDao;
    private	static final Logger LOGGER = LoggerFactory.getLogger(VerificationCodesServiceImpl.class);
    @Autowired
    public VerificationCodesServiceImpl(EmailService emailService, VerificationCodeDao verificationCodeDao, UserDao userDao) {
        this.emailService = emailService;
        this.verificationCodeDao = verificationCodeDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void sendForgotPasswordCode(String email) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        verificationCodeDao.deleteVerificationCodes(email);
        VerificationCode verificationCode = new VerificationCode(String.format("%06d", number), email, LocalDateTime.now().plusMinutes(10));
        verificationCodeDao.saveVerificationCode(verificationCode);
        LOGGER.info("New verification code stored into database for user {}", email);

        String lang = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new).getLocale();
        emailService.sendForgotPasswordEmail(verificationCode, new Locale(lang));
    }

    @Override
    @Transactional
    public boolean verifyForgotPasswordCode(String email, String code) {
        return verificationCodeDao.verifyForgotPasswordCode(email, code) &&
                verificationCodeDao.deleteVerificationCodes(email);
    }
}
