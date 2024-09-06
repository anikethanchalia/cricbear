package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class UserController {

    @Autowired
    private UserService userService;

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return registeredUser;
    }

    @PostMapping("/login")
    public Role loginUser(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        boolean isAuthenticated = userService.authenticateUser(username, password);
        if (isAuthenticated) {
            return userService.getUserRole(user.get("username"));
        } else {
            return  null;
        }
    }

    @PostMapping("/getRole")
    public Role getUserRole(@RequestBody Map<String, String> user) {
        return userService.getUserRole(user.get("username"));
    }

    @GetMapping("/setUserRole/{username}/{role}")
    public String setUserRole(@PathVariable String username, @PathVariable Role role) {
        return userService.setUserRole(username,role);
    }

}