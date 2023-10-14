package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.ProfilePicture;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.user.InvalidUserException;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.models.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;
    private final VerificationCodesService verificationCodesService;

    private final EmailService emailService;

    private	static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int BAN_DURATION = 3;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, final SecurityService securityService,
                           final VerificationCodesService verificationCodesService, final EmailService emailService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
        this.verificationCodesService = verificationCodesService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    @Override
    public Page<User> getStudents(String query, int page) {
        if (query == null) query = "";
        int countStudents = userDao.getStudentsQuantity(query);
        int currentPage = Page.getSafePagePosition(page, countStudents, PAGE_SIZE);
        List<User> users = userDao.getStudents(query, currentPage, PAGE_SIZE);
        return new Page<>(users, currentPage, PAGE_SIZE, countStudents);
    }

    //@Transactional    //TODO: enable transactional
    @Override
    public void create(String email, String password, UUID careerId, Role role) {
        final String lang = LocaleContextHolder.getLocale().getLanguage();
        userDao.create(email, passwordEncoder.encode(password), careerId, lang, role);
    }

    @Transactional
    @Override
    public void updateProfile(String firstName, String lastName, String username, MultipartFile profilePicture) {
        User user = securityService.getCurrentUserOrThrow();
        user.setProfileData(firstName, lastName, username);
        boolean success = userDao.update(user);
        if (!success) throw new InvalidUserException();
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                userDao.updateProfilePicture(user.getUserId(), profilePicture.getBytes());
            } catch (IOException e) {
                LOGGER.error("Error while updating profile picture for user {}", user.getUserId());
                throw new InvalidFileException();
            }
        }
    }

    @Transactional
    @Override
    public Optional<byte[]> getProfilePicture(UUID userId) {
        Optional<ProfilePicture> picture = userDao.getProfilePicture(userId);
        return picture.map(ProfilePicture::getPicture);
    }

    @Transactional
    @Override
    public void updateCurrentUserPassword(String password) {
        userDao.updatePassword(securityService.getCurrentUserOrThrow().getUserId(), passwordEncoder.encode(password));
    }

    @Transactional
    @Override
    public boolean updateUserPasswordWithCode(String email, String code, String password) {
        LOGGER.info("Attempting to update forgotten password for user {}", email);
        return verificationCodesService.verifyForgotPasswordCode(email, code) && userDao.updatePasswordForUserWithEmail(email, passwordEncoder.encode(password));
    }

    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    // @Scheduled(cron = "0 * * * * *") // Every minute
    @Transactional
    @Override
    public void unbanUsers() {
        LOGGER.info("Unbanning users at {}", LocalDateTime.now());
        userDao.unbanUsers();
    }

    @Transactional
    @Override
    public void unbanUser(UUID userId) {
        if (!userDao.unbanUser(userId))
            throw new InvalidUserException();
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        emailService.sendUnbanEmail(user);
    }

    @Transactional
    @Override
   public void banUser(UUID userId, String reason) {
        User admin = securityService.getCurrentUserOrThrow();
        if (!userDao.banUser(userId, admin.getUserId(), LocalDateTime.now().plusDays(BAN_DURATION), reason))
            throw new InvalidUserException();
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        emailService.sendBanEmail(user, reason, BAN_DURATION);
    }


}
