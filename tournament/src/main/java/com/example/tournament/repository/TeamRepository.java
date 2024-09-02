package com.example.tournament.repository;

import com.example.tournament.model.Team;
import com.example.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByTeamName(String name);
}
