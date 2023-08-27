package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;
import org.springframework.stereotype.Repository;

@Repository
class UserDaoImpl implements UserDao{
    @Override
    public User create(final String email, final String password) {
        return new User(email, password);
    }
}
