package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;

import java.util.Optional;

public interface UserDao {
    User create(String email);
    Optional<User> findById(long id);
}
