package com.example.userAuthorisation.repository;

import com.example.userAuthorisation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
        User findByUsername(String username);
        User findByUid(Integer uid);
}

