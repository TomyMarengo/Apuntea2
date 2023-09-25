package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    void create(String email, String password, UUID institutionId, UUID careerId, String lang);
    Optional<User> findByEmail(String email);
}
