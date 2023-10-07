package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    void create(String email, String password, UUID careerId, Role role);
    void update(User user, MultipartFile multipartFile);

    Optional<byte[]> getProfilePicture(UUID userId);
}
