package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.FileExtensionException;
import ar.edu.itba.paw.models.exceptions.FileSizeException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidCareerException;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.Image;
import ar.edu.itba.paw.models.user.Role;
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

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};
    private static final int MAX_PROFILE_PICTURE_SIZE = 64 * 1024 * 1024; // 64 MB

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

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(UUID userId) {
        if (userId == null) return Optional.empty();
        return userDao.findById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> getUsers(String query, String status, UUID followedBy, int page, int pageSize) {
        if (query == null) query = "";
        UserStatus userStatus = UserStatus.fromString(status);
        int countStudents = userDao.getUsersQuantity(query, userStatus, followedBy);
        int currentPage = Page.getSafePagePosition(page, countStudents, pageSize);
        List<User> users = userDao.getUsers(query, userStatus, followedBy, currentPage, pageSize);
        return new Page<>(users, currentPage, pageSize, countStudents);
    }

    @Transactional
    @Override
    public boolean follow(UUID followedId) {
        userDao.findById(followedId).orElseThrow(UserNotFoundException::new);
        return userDao.follow(securityService.getCurrentUserOrThrow().getUserId(), followedId);
    }

    @Transactional
    @Override
    public boolean unfollow(UUID followedId) {
        userDao.findById(followedId).orElseThrow(UserNotFoundException::new);
        return userDao.unfollow(securityService.getCurrentUserOrThrow().getUserId(), followedId);
    }

    /*@Transactional
    @Override
    public Collection<User> getFollows() {
        User currentUser = securityService.getCurrentUserOrThrow();
        return currentUser.getUsersFollowing();
    }
    */
    @Transactional(readOnly = true)
    @Override
    public boolean isFollowing(UUID followedId) {
        User currentUser = securityService.getCurrentUserOrThrow();
        userDao.findById(followedId).orElseThrow(UserNotFoundException::new);
        return userDao.isFollowing(currentUser.getUserId(), followedId);
    }

    @Transactional
    @Override
    public UUID create(String email, String password, UUID careerId, Role role) {
        final String lang = LocaleContextHolder.getLocale().getLanguage();
        Career career = careerDao.getCareerById(careerId).orElseThrow(InvalidCareerException::new);
        UUID userId = userDao.create(email, passwordEncoder.encode(password), career, lang, Collections.singleton(role));
        LOGGER.info("User with email {} created (registered language: {})", email, lang);
        return userId;
    }

    @Transactional
    @Override
    public void updateProfile(String firstName, String lastName, String username, byte[] profilePictureBytes, String imageExtension, UUID careerId) {
        User user = securityService.getCurrentUserOrThrow();
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (username != null) user.setUsername(username);
        if (careerId != null) {
            Career career = careerDao.getCareerById(careerId).orElseThrow(InvalidCareerException::new);
            user.setCareer(career);
        }
        if (profilePictureBytes != null) {
            if (Arrays.stream(ALLOWED_EXTENSIONS).noneMatch(imageExtension::equalsIgnoreCase)) {
                LOGGER.warn("Invalid image extension: {}", imageExtension);
                throw new FileExtensionException(ALLOWED_EXTENSIONS);
            }
            if (profilePictureBytes.length > MAX_PROFILE_PICTURE_SIZE) {
                LOGGER.warn("Image too large: {}", profilePictureBytes.length);
                throw new FileSizeException(MAX_PROFILE_PICTURE_SIZE);
            }
            userDao.updateProfilePicture(user, new Image(profilePictureBytes));

        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<byte[]> getProfilePictureByUserId(UUID userId) {
        Optional<User> maybeUser = userDao.findById(userId);
        if (!maybeUser.isPresent()) {
            LOGGER.warn("Error while getting profile picture for user with id: {}", userId);
            throw new UserNotFoundException();
        }
        return maybeUser.map(User::getProfilePicture).map(Image::getPicture);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<byte[]> getProfilePictureById(UUID pictureId) {
        return userDao.getProfilePicture(pictureId);
    }

    @Transactional
    @Override
    public void updateCurrentUserPassword(String password) {
        User user = securityService.getCurrentUserOrThrow();
        user.setPassword(passwordEncoder.encode(password));
        LOGGER.info("Password updated for user with id: {}", user.getUserId());
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
            LOGGER.warn("Error while unbanning user with id: {}", userId);
//            throw new InvalidUserException();
        } else {
            emailService.sendUnbanEmail(user);
        }
    }

    @Transactional
    @Override
    public void banUser(UUID userId, String reason) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        User admin = securityService.getAdminOrThrow();
        if (!userDao.banUser(user, admin, LocalDateTime.now().plusDays(BAN_DURATION), reason)) {
            LOGGER.warn("Could not ban user with id: {}", userId);
//            throw new InvalidUserException();
        } else {
            emailService.sendBanEmail(user, reason, BAN_DURATION);
        }
    }

    @Transactional
    @Override
    public void updateNotificationsEnabled(boolean notificationsEnabled) {
        User user = securityService.getCurrentUserOrThrow();
        user.getNotificationsEnabled(notificationsEnabled);
        LOGGER.info("Notifications enabled updated for user with email: {}, updated to {}", user.getEmail(), user.getNotificationsEnabled());
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
