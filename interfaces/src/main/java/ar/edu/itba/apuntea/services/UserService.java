package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<User> findById(final UUID id);

}
