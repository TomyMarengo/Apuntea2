package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ProfilePicture;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    void create(String email, String password, UUID careerId, String lang, Role role);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean update(User user);
    Optional<ProfilePicture> getProfilePicture(UUID userId);
    void updateProfilePicture(UUID userId, byte[] profilePicture);
    void updatePassword(UUID userId, String password);
    boolean updatePasswordForUserWithEmail(String email, String password);
    void unbanUsers();
    boolean banUser(UUID userId, UUID adminId, LocalDateTime endDate);
    boolean unbanUser(UUID userId);
    List<User> getStudents(String query, int pageNum);
    int getStudentsQuantity(String query);
}
