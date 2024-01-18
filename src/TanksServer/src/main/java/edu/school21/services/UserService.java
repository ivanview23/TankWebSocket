package edu.school21.services;

import edu.school21.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> signUp(String name, String password);

    boolean signIn(String name, String password);

    boolean deleteUser(User user);

    boolean updateUser(User user);

    Optional<User> getUserByName(String name);

    List<User> getUsers();
}
