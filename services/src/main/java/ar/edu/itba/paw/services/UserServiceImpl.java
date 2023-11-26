package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.institutional.InvalidCareerException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.Image;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.user.InvalidUserException;
import ar.edu.itba.paw.models.user.UserStatus;
import ar.edu.itba.paw.persistence.CareerDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.models.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final CareerDao careerDao;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;
    private final VerificationCodesService verificationCodesService;

    private final EmailService emailService;

    private	static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int BAN_DURATION = 3;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final CareerDao careerDao, PasswordEncoder passwordEncoder, final SecurityService securityService,
                           final VerificationCodesService verificationCodesService, final EmailService emailService) {
        this.userDao = userDao;
        this.careerDao = careerDao;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
        this.verificationCodesService = verificationCodesService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public Optional<User> findById(UUID userId) {
        return userDao.findById(userId);
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
    public Page<User> getStudents(String query, String status, int page) {
        if (query == null) query = "";
        UserStatus userStatus = UserStatus.fromString(status);
        int countStudents = userDao.getStudentsQuantity(query, userStatus);
        int currentPage = Page.getSafePagePosition(page, countStudents, PAGE_SIZE);
        List<User> users = userDao.getStudents(query, userStatus, currentPage, PAGE_SIZE);
        return new Page<>(users, currentPage, PAGE_SIZE, countStudents);
    }

    @Transactional
    @Override
    public void follow(UUID followedId) {
        User currentUser = securityService.getCurrentUserOrThrow();
        userDao.follow(currentUser, followedId);
    }

    @Transactional
    @Override
    public void unfollow(UUID followedId) {
        User currentUser = securityService.getCurrentUserOrThrow();
        userDao.unfollow(currentUser, followedId);
    }

    @Transactional
    @Override
    public Collection<User> getFollows() {
        User currentUser = securityService.getCurrentUserOrThrow();
        return currentUser.getUsersFollowing();
    }

    @Transactional
    @Override
    public boolean isFollowing(UUID followedId) {
        User currentUser = securityService.getCurrentUserOrThrow();
        User followed = userDao.findById(followedId).orElseThrow(UserNotFoundException::new);
        return currentUser.getUsersFollowing().contains(followed);
    }

    @Transactional
    @Override
    public void create(String email, String password, UUID careerId, Role role) {
        final String lang = LocaleContextHolder.getLocale().getLanguage();
        Career career = careerDao.getCareerById(careerId).orElseThrow(InvalidCareerException::new);
        userDao.create(email, passwordEncoder.encode(password), career, lang, Collections.singleton(role));
        LOGGER.info("User with email {} created (registered language: {})", email, lang);
    }

    @Transactional
    @Override
    public void updateProfile(String firstName, String lastName, String username, MultipartFile profilePicture, UUID careerId) {
        User user = securityService.getCurrentUserOrThrow();
        Career career = careerDao.getCareerById(careerId).orElseThrow(InvalidCareerException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setCareer(career);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                userDao.updateProfilePicture(user, new Image(profilePicture.getBytes()));
            } catch (IOException e) {
                LOGGER.error("Error while updating profile picture for user with id: {}", user.getUserId());
                throw new InvalidFileException();
            }
        }
    }

    @Transactional
    @Override
    public Optional<byte[]> getProfilePicture(UUID userId) {
        Optional<User> maybeUser = userDao.findById(userId);
        if (!maybeUser.isPresent()) {
            LOGGER.warn("Error while getting profile picture for user with id: {}", userId);
            throw new UserNotFoundException();
        }
        return maybeUser.map(User::getProfilePicture).map(Image::getPicture);
    }

    @Transactional
    @Override
    public void updateCurrentUserPassword(String password) {
        User user = securityService.getCurrentUserOrThrow();
        user.setPassword(passwordEncoder.encode(password));
        LOGGER.info("Password updated for user with id: {}", user.getUserId());
    }

    @Transactional
    @Override
    public boolean updateUserPasswordWithCode(String email, String code, String password) {
        LOGGER.info("Updating forgotten password for user with email: {}", email);
        User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        boolean success = verificationCodesService.verifyForgotPasswordCode(email, code);
        if (!success) {
            LOGGER.warn("Invalid code for user with email: {}", email);
            return false;
        }
        user.setPassword(passwordEncoder.encode(password));
        return true;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    @Transactional
    @Override
    public void unbanUsers() {
        int unbannedUsers = userDao.unbanUsers();
        LOGGER.info("{} users unbanned", unbannedUsers);
    }

    @Transactional
    @Override
    public void unbanUser(UUID userId) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!userDao.unbanUser(user)) {
            LOGGER.error("Error while unbanning user with id: {}", userId);
            throw new InvalidUserException();
        }
        emailService.sendUnbanEmail(user);
    }

    @Transactional
    @Override
    public void banUser(UUID userId, String reason) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        User admin = securityService.getAdminOrThrow();
        if (!userDao.banUser(user, admin, LocalDateTime.now().plusDays(BAN_DURATION), reason)) {
            LOGGER.error("Error while banning user with id: {}", userId);
            throw new InvalidUserException();
        }
        emailService.sendBanEmail(user, reason, BAN_DURATION);
    }

    @Transactional
    @Override
    public void updateNotificationsEnabled(boolean notificationsEnabled) {
        User user = securityService.getCurrentUserOrThrow();
        user.setNotificationsEnabled(notificationsEnabled);
        LOGGER.info("Notifications enabled updated for user with id: {}, updated to {}", user.getUserId(), user.hasNotificationsEnabled());
    }

    /* Function to avoid using formula inside User,
     * since formulas are not lazy,
     * and users are fetched constantly */
    @Transactional
    @Override
    public float getAvgScore(UUID userId) {
        return userDao.getAvgScore(userId);
    }

    @Transactional
    @Override
    public User getNoteOwner(UUID userId, User currentUser) {
        if (currentUser != null && currentUser.getUserId().equals(userId)) {
            return currentUser;
        } else {
            return userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        }
    }
}
