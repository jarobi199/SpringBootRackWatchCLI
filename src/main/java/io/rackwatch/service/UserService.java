package io.rackwatch.service;

import io.rackwatch.authentication.PasswordEncryptor;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.Role;
import io.rackwatch.model.User;
import io.rackwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void addUser(String fullName, String username, String password, Role role) {
        User user = new User(fullName, username, PasswordEncryptor.encrypt(password), role);
        userRepository.save(user);
    }

    public void changePassword(String newPassword) {
        String encodedPassword = PasswordEncryptor.encrypt(newPassword);
        SessionContext.getUser().setPassword(encodedPassword);
        userRepository.save(SessionContext.getUser());
        System.out.println("Your password has been changed!");
    }

    public void setAlertLeadDays(int alertLeadDays) {
        SessionContext.getUser().setAlertLeadDays(alertLeadDays);
        userRepository.save(SessionContext.getUser());
        System.out.println("The alert lead days has been set to " + alertLeadDays + ".");
    }

    public void deleteUser(String username) {
        Optional<User> toDelete = userRepository.findByUsername(username);
        toDelete.ifPresent(user -> {
            userRepository.delete(user);
            System.out.println("User " + username + " has been deleted!");
        });
    }

}
