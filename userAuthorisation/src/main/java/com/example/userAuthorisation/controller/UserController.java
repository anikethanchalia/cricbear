package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity <List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }
}
