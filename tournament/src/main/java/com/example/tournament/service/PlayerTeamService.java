package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.model.DTO.PlayerTeamDTOConverter;
import com.example.tournament.repository.PlayerTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerTeamService {

    @Autowired
    private PlayerTeamRepository playerTeamRepository;

    private static final int MAX_PLAYERS = 15;
    private static final int MAX_OVERSEAS_PLAYERS = 5;
    private static final int MAX_BATSMEN = 6;
    private static final int MAX_BOWLERS = 4;
    private static final int MAX_ALLROUNDER = 4;
    private static final int MAX_WICKETKEEPERS = 1;

    // Retrieve all player teams
    public List<PlayerTeam> getAllPlayerTeams() {
        return playerTeamRepository.findAll();
    }

    // Get a specific player team by ID
    public Optional<PlayerTeam> getPlayerTeamById(int tpid) {
        return playerTeamRepository.findById(tpid);
    }

    // Get all player teams for a specific team ID
    public List<PlayerTeamDTO> getPlayerTeamsByTeamId(int teamId) {
        List<Object[]> results = playerTeamRepository.findPlayerTeamByTeamId(teamId);
        List<PlayerTeamDTO> playerTeamDTOs = new ArrayList<>();

        for (Object[] result : results) {
            PlayerTeamDTO playerTeamDTO = PlayerTeamDTOConverter.convert(result);
            playerTeamDTOs.add(playerTeamDTO);
        }

        return playerTeamDTOs;
    }
    public PlayerTeam savePlayerTeam(PlayerTeamDTO playerTeamDTO) {
        int teamId = playerTeamDTO.getTeamId();
        int pid = playerTeamDTO.getPid();

        PlayerRole playerRole = playerTeamDTO.getPlayerRole();
        boolean overseas = playerTeamDTO.isOverseas();

        // Check if playerRole is null
        if (playerRole == null) {
            throw new IllegalArgumentException("Player role cannot be null.");
        }

        // Retrieve existing player teams for the team
        long overseasCount = playerTeamRepository.countOverseasPlayers(teamId);
        long roleCount = playerTeamRepository.countPlayersByRole(teamId, playerRole);
        List<PlayerTeam> existingPlayerTeams = playerTeamRepository.findByTeamId(teamId);

        // Check if the player is already in the team
        boolean playerExists = existingPlayerTeams.stream()
                .anyMatch(pt -> pt.getPid() == pid);

        if (playerExists) {
            throw new IllegalArgumentException("Player is already part of the team.");
        }

        // Check player count limit
        if (existingPlayerTeams.size() >= MAX_PLAYERS) {
            throw new IllegalArgumentException("Team already has the maximum number of players (15).");
        }

        // Validate overseas players
        if (overseas && overseasCount >= MAX_OVERSEAS_PLAYERS) {
            throw new IllegalArgumentException("Team already has the maximum number of overseas players (5).");
        }

        // Validate role count based on player role
        switch (playerRole) {
            case BATSMAN:
                if (roleCount >= MAX_BATSMEN) {
                    throw new IllegalArgumentException("Team already has the maximum number of batsmen (6).");
                }
                break;
            case BOWLER:
                if (roleCount >= MAX_BOWLERS) {
                    throw new IllegalArgumentException("Team already has the maximum number of bowlers (4).");
                }
                break;
            case ALLROUNDER:
                if (roleCount >= MAX_ALLROUNDER) {
                    throw new IllegalArgumentException("Team already has the maximum number of all-rounders (3).");
                }
                break;
            case WICKETKEEPER:
                if (roleCount >= MAX_WICKETKEEPERS) {
                    throw new IllegalArgumentException("Team already has the maximum number of wicketkeepers (2).");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid player role.");
        }

        // Convert DTO to entity
        PlayerTeam playerTeam = new PlayerTeam();
        playerTeam.setTeamId(teamId);
        playerTeam.setPid(pid);
        playerTeam.setPlayerRole(playerRole);
        playerTeam.setOverseas(overseas);

        // Save and return the entity
        return playerTeamRepository.save(playerTeam);
    }




    // Update an existing player team
    public PlayerTeam updatePlayerTeam(int tpid, PlayerTeam updatedPlayerTeam) {
        Optional<PlayerTeam> existingPlayerTeamOpt = playerTeamRepository.findById(tpid);

        if (existingPlayerTeamOpt.isPresent()) {
            PlayerTeam playerTeam = existingPlayerTeamOpt.get();

            // Update player details
            playerTeam.setRunsScored(updatedPlayerTeam.getRunsScored());
            playerTeam.setBalls(updatedPlayerTeam.getBalls());
            playerTeam.setWickets(updatedPlayerTeam.getWickets());
            playerTeam.setOvers(updatedPlayerTeam.getOvers());
            playerTeam.setRunsGiven(updatedPlayerTeam.getRunsGiven());

            // Save and return the updated entity
            return playerTeamRepository.save(playerTeam);
        } else {
            return null; // Handle case where the player team is not found
        }
    }

    // Delete a player team by ID
    public void deletePlayerTeam(int tpid) {
        playerTeamRepository.deleteById(tpid);
    }

    // Count players by team ID
    public int countByTeamId(int teamId) {
        return playerTeamRepository.countByTeamId(teamId);
    }
}
