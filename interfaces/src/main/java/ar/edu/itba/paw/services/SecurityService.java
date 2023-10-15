package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.user.User;

import java.util.Optional;

public interface SecurityService {
    Optional<User> getCurrentUser();
    Optional<String> getCurrentUserEmail();
    boolean currentUserPasswordMatches(String password);
    User getCurrentUserOrThrow();
    User getAdminOrThrow();
}