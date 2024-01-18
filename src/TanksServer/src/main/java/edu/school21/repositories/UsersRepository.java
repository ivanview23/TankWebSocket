package edu.school21.repositories;

import edu.school21.domain.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Optional<User> findByName(String name);
    List<User> findAll();
    boolean save(String name, String password);
    boolean update(User user);
    boolean delete(User user);
}
