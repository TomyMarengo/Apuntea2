package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.user.RequiredAdminException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService{
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<String> getCurrentUserEmail() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return Optional.of(securityContext.getAuthentication().getName());
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<User> getCurrentUser() {
        final Optional<String> maybeEmail = getCurrentUserEmail();
        if (!maybeEmail.isPresent()){
            return Optional.empty();
        }
        return userDao.findByEmail(maybeEmail.get());
    }

    @Transactional
    @Override
    public User getCurrentUserOrThrow() {
        return getCurrentUser().orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public User getAdminOrThrow() {
        return getCurrentUser().filter(User::isAdmin).orElseThrow(RequiredAdminException::new);
    }

    @Transactional
    @Override
    public boolean currentUserPasswordMatches(String password) {
        return passwordEncoder.matches(password, getCurrentUser().map(User::getPassword).orElse(""));
    }
}
