package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService{
    @Autowired
    private UserDao userDao;

    @Override
    public Optional<String> getCurrentUserEmail() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null && securityContext.getAuthentication() != null) {
            return Optional.of(securityContext.getAuthentication().getName());
        }
        return Optional.empty();
    }

    @Cacheable
    @Override
    public Optional<User> getCurrentUser() {
        final Optional<String> mayBeEmail = getCurrentUserEmail();

        if (!mayBeEmail.isPresent()){
            return Optional.empty();
        }

        return userDao.findByEmail(mayBeEmail.get());
    }

    @Override
    public User getCurrentUserOrThrow() {
        return getCurrentUser().orElseThrow(UserNotFoundException::new);
    }
}
