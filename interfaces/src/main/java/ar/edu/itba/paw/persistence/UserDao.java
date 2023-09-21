package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    User create(String email, String password);
    Optional<User> findByEmail(String email);
}
