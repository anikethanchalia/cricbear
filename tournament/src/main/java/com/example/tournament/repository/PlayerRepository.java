package com.example.tournament.repository;

import com.example.tournament.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    public Player findByName(String name);

    public Player findByPid(int pid);
    void deleteByName(String name);

}