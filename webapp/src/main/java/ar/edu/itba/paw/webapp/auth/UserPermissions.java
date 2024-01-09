package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserPermissions {
    private final SecurityService securityService;

    @Autowired
    public UserPermissions(final SecurityService securityService) {
        this.securityService = securityService;
    }

    public boolean isCurrentUser(UUID userId) {
        Optional<User> maybeUser = securityService.getCurrentUser();
        return maybeUser.isPresent() && maybeUser.get().getUserId().equals(userId);
    }
}
