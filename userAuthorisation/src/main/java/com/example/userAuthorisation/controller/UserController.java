package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<Role> loginUser(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        boolean isAuthenticated = userService.authenticateUser(username, password);
        if (isAuthenticated) {
            Role role = userService.getUserRole(username);
            if (role != null) {
                return ResponseEntity.ok(role);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Get user role by username
    @PostMapping("/getRole")
    public ResponseEntity<Role> getUserRole(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        Role role = userService.getUserRole(username);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Set user role
    @PutMapping("/setUserRole/{username}/{role}")
    public ResponseEntity<String> setUserRole(@PathVariable String username, @PathVariable Role role) {
        boolean isUpdated = userService.setUserRole(username, role);
        if (isUpdated) {
            return new ResponseEntity<>("Role updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User or role not found", HttpStatus.NOT_FOUND);
        }
    }
}
