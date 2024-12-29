package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findById(UUID userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    UUID create(String email, String password, UUID careerId, Role role);

    void updateProfile(String firstName, String lastName, String username, UUID careerId);

    UUID updateProfilePicture(byte[] profilePictureBytes, String imageExtension);

    Optional<byte[]> getProfilePictureByUserId(UUID userId);

    Optional<byte[]> getProfilePictureById(UUID pictureId);

    void updateCurrentUserPassword(String password);

    void unbanUsers();

    void unbanUser(UUID userId);

    void banUser(UUID userId, String reason);

    Page<User> getUsers(String query, String status, UUID followedBy, UUID following, int page, int pageSize);

    boolean isFollowing(UUID followedId);

    boolean follow(UUID followedId);

    boolean unfollow(UUID followedId);

    void updateNotificationsEnabled(boolean notificationsEnabled);

    float getAvgScore(UUID userId);

    User getNoteOwner(UUID userId, User currentUser);
}
