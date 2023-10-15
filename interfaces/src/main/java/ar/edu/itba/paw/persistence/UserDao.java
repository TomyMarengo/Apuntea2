package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.ProfilePicture;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    void create(String email, String password, UUID careerId, String lang, Role role);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(UUID userId);
    boolean update(User user);
    Optional<ProfilePicture> getProfilePicture(UUID userId);
    void updateProfilePicture(UUID userId, byte[] profilePicture);
    void updatePassword(UUID userId, String password);
    boolean updatePasswordForUserWithEmail(String email, String password);
    int unbanUsers();
    boolean banUser(UUID userId, UUID adminId, LocalDateTime endDate, String reason);
    boolean unbanUser(UUID userId);
    List<User> getStudents(String query, int pageNum, int pageSize);
    int getStudentsQuantity(String query);
}
