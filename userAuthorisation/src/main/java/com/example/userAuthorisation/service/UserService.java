package com.example.userAuthorisation.service;

import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userAuthorisation.model.Role;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register a new user
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        return userRepository.save(user);
    }

    // Update an existing user
    public User updateUser(int id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword())); // Encrypt password
        user.setEmail(userDetails.getEmail());
        user.setGender(userDetails.getGender());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    // Assign a role to a user
    public User assignRole(int id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

    // Login a user
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid username or password");
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Identify a user's role
    public String identifyUserRole(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole().toString();
    }
}
