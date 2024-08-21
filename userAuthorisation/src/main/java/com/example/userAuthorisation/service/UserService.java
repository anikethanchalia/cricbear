package com.example.userAuthorisation.service;

import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List< User > getAllUser() {
        return this.userRepository.findAll();
    }
}
