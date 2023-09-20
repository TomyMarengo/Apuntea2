package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findByEmail(String email);

    User create(String email, String password);

}
