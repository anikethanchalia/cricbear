package com.example.tournament.service;

import com.example.tournament.model.Team;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team create(Team team) {
        if (teamRepository.findByTeamName(team.getTeamName()) == null)
            return teamRepository.save(team);
        return null;
    }

    public Team getById(int id) {
        return teamRepository.findById(id).orElse(null);
    }

    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    public Team getByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }

    public Team update(int teamId, Team updatedTeam) {
        // Retrieve the existing team by ID
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setMatchesPlayed(updatedTeam.getMatchesPlayed());
        existingTeam.setMatchesWon(updatedTeam.getMatchesWon());
        existingTeam.setMatchesLost(updatedTeam.getMatchesLost());
        existingTeam.setMatchesDrawn(updatedTeam.getMatchesDrawn());
        existingTeam.setMatchesAbandoned(updatedTeam.getMatchesAbandoned());
        existingTeam.setPoints(updatedTeam.getPoints());
        existingTeam.setNrr(updatedTeam.getNrr());

        return teamRepository.save(existingTeam);
    }

    public void delete(Integer id) {
        teamRepository.deleteById(id);
    }

    public Team updateAfterResults(Team teamBatting, int matchesPlayed, int matchesWon, int matchesLost, int matcheDrawn, int matchesAbandoned, int points, double nrr) {
        teamBatting.setMatchesPlayed(teamBatting.getMatchesPlayed() + matchesPlayed);
        teamBatting.setMatchesWon(teamBatting.getMatchesWon() + matchesWon);
        teamBatting.setMatchesLost(teamBatting.getMatchesLost() + matchesLost);
        teamBatting.setMatchesDrawn(teamBatting.getMatchesDrawn()+matcheDrawn);
        teamBatting.setMatchesAbandoned(teamBatting.getMatchesAbandoned()+matchesAbandoned);
        teamBatting.setPoints(teamBatting.getPoints()+points);
        teamBatting.setNrr(teamBatting.getNrr()+nrr);
        return teamRepository.save(teamBatting);
    }
}
