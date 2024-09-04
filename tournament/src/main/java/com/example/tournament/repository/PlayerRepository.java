package com.example.tournament.repository;

import com.example.tournament.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    public Player findByFirstName(String firstName);
    public List<Player> findByTeamid(int teamid);
    public Player findByPid(int pid);
    void deleteByFirstName(String firstName);
    @Query("SELECT COUNT(*) from Player where teamid = :teamid")
    public int countByTeamid(int teamid);
}