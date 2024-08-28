package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userAuthorisation.model.Role;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    // Update an existing user
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Assign a role to a user
    @PutMapping("/assignRole/{id}")
    public ResponseEntity<User> assignRole(@PathVariable int id, Role role) {
        User updatedUser = userService.assignRole(id,  role);
        return ResponseEntity.ok(updatedUser);
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User loggedInUser = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Identify a user's role
    @GetMapping("/role/{id}")
    public ResponseEntity<String> identifyUserRole(@PathVariable int id) {
        String role = userService.identifyUserRole(id);
        return ResponseEntity.ok(role);
    }
}
