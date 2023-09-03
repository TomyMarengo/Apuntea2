package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;

import java.util.Optional;

public interface UserDao {
    User create(long userId, String email, String password);

    Optional<User> findById(long id);
}
