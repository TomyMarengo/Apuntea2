package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ProfilePicture;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

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
}
