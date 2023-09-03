package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.User;

import java.util.Optional;

public interface UserService {
    User createUser(long userId, String email, String password);

    Optional<User> findById(final long id);

}
