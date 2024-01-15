package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findById(UUID userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    UUID create(String email, String password, UUID careerId, Role role);

    void updateProfile(String firstName, String lastName, String username, byte[] profilePictureBytes, UUID careerId);

    Optional<byte[]> getProfilePictureByUserId(UUID userId);

    Optional<byte[]> getProfilePictureById(UUID pictureId);

    void updateCurrentUserPassword(String password);

    boolean updateUserPasswordWithCode(String email, String code, String password);

    void unbanUsers();

    void unbanUser(UUID userId);

    void banUser(UUID userId, String reason);

    Page<User> getUsers(String query, String status, UUID followedBy, int page, int pageSize);

    boolean follow(UUID followedId);

    boolean unfollow(UUID followedId);

//    Collection<User> getFollows();

//    boolean isFollowing(UUID followedId);
    
    void updateNotificationsEnabled(boolean notificationsEnabled);

    float getAvgScore(UUID userId);

    User getNoteOwner(UUID userId, User currentUser);
}
