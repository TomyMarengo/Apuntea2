package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;

public interface UserDao {
    User create(String email, String password);
}
