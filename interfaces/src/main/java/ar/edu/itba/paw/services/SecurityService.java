package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface SecurityService {
    Optional<User> getCurrentUser();
    Optional<String> getCurrentUserEmail();

    User getCurrentUserOrThrow();
}