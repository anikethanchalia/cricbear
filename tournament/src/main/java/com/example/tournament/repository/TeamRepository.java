package com.example.tournament.repository;

import com.example.tournament.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByTeamName(String name);
    Team findByTeamId(Integer id);

    @Query("select t from Team t order by t.points desc")
    List<Team> findAllTeamsInDescendingOrder();
}
