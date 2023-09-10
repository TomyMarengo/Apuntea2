package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    User create(String email);
    Optional<User> findById(UUID userId);

    User createIfNotExists(String email);
}
