package edu.school21.services;

import edu.school21.domain.User;
import edu.school21.repositories.UsersRepositoryImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UsersRepositoryImpl usersRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public Optional<User> signUp(String name, String password) {
        Optional<User> user = usersRepository.findByName(name);
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword() ))
                return user;
        }
        return Optional.empty();
    }

    @Override
    public boolean signIn(String name, String password) {
        if(usersRepository.findByName(name).isPresent())
            return false;
        String hashPassword = passwordEncoder.encode(password);
        return usersRepository.save(name, hashPassword);
    }

    @Override
    public boolean deleteUser(User user) {
        return usersRepository.delete(user);
    }

    @Override
    public boolean updateUser(User user) {
        return usersRepository.update(user);
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return usersRepository.findByName(name);
    }

    @Override
    public List<User> getUsers() {
        return usersRepository.findAll();
    }
}
