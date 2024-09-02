package com.example.tournament.repository;

import com.example.tournament.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    public Player findByFirstName(String firstName);
    public Player findByTeamid(int tid);
    public Player findByPid(int pid);
    void deleteByFirstName(String firstName);
}