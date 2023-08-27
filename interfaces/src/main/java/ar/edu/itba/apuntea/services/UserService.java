package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.User;

public interface UserService {
    User createUser(String email, String password);

}
