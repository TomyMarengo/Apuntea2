package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.exceptions.InvalidUserException;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.models.User;

import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    private final SecurityService securityService;

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, SecurityService securityService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    //    @Transactional
    @Override
    public void create(String email, String password, UUID careerId, Role role) {
        final String lang = LocaleContextHolder.getLocale().getLanguage();
        userDao.create(email, passwordEncoder.encode(password), careerId, lang, role);
    }

    @Override
    public void update(User user) {
        user.setUserId(securityService.getCurrentUserOrThrow().getUserId());
        boolean success = userDao.update(user);
        if (!success) throw new InvalidUserException();
    }
}
