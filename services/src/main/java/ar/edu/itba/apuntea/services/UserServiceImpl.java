package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.apuntea.models.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(String email, String password) {
        return userDao.create(email, password);
    }
}
