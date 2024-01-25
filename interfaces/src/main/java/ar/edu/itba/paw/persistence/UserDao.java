package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.Image;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.UserStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    UUID create(String email, String password, Career career, String lang, Collection<Role> role);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(UUID userId);
    void updateProfilePicture(User user, Image img);
    boolean isFollowing(UUID followerId, UUID followedId);
    boolean follow(UUID followerId, UUID followedId);
    boolean unfollow(UUID followerId, UUID followedId);
    int unbanUsers();
    boolean banUser(User user, User admin, LocalDateTime endDate, String reason);
    boolean unbanUser(User user);
    List<User> getUsers(String query, UserStatus status, UUID followedBy, int pageNum, int pageSize);
    int getUsersQuantity(String query, UserStatus status, UUID followedBy);
    float getAvgScore(UUID userId);

    Optional<byte[]> getProfilePicture(UUID pictureId);

}
