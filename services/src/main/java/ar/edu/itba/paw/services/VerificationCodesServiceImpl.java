package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.VerificationCode;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationCodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
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
    public void sendForgotPasswordCode(final String email) {
        final Random rnd = new Random();
        final int number = rnd.nextInt(999999);

        final Optional<User> maybeUser = userDao.findByEmail(email);//.orElseThrow(UserNotFoundException::new);
        if (!maybeUser.isPresent()) {
            LOGGER.info("User with email {} not found", email);
            return;
        }
        final User user = maybeUser.get();

        VerificationCode verificationCode = verificationCodeDao.saveVerificationCode(String.format("%06d", number), user, LocalDateTime.now().plusMinutes(10));
        LOGGER.info("New verification code stored into database for user {}", user.getEmail());
        emailService.sendForgotPasswordEmail(verificationCode, user.getLocale());
    }

    @Override
    @Transactional
    public boolean verifyForgotPasswordCode(final String email, final String code) {
        return verificationCodeDao.verifyForgotPasswordCode(email, code) && verificationCodeDao.deleteVerificationCodes(email);
    }
}
