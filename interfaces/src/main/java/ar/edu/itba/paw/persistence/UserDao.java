package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    void create(String email, String password, UUID careerId, String lang, Role role);
    Optional<User> findByEmail(String email);
}
