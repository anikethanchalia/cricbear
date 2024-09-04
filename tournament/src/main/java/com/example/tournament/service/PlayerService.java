package com.example.tournament.service;

import com.example.tournament.model.Player;
import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player create(Player player) {
        if (playerRepository.findByFirstName(player.getFirstName()) == null)
            return playerRepository.save(player);
        return null;
    }

    public List<Player> getAll() {
        return playerRepository.findAll();
    }


    public List<Player> getById(Integer id) {
        return playerRepository.findByTeamid(id);
    }

    public int countByTeamid(int teamid){
        return playerRepository.countByTeamid(teamid);
    }

    public Player update(int playerId, Player updatedPlayer) {
        // Retrieve the existing player by ID
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        existingPlayer.setFirstName(updatedPlayer.getFirstName());
        existingPlayer.setLastName(updatedPlayer.getLastName());
        existingPlayer.setOverseas(updatedPlayer.isOverseas());
        existingPlayer.setPlayerRole(updatedPlayer.getPlayerRole());
        existingPlayer.setRunsScored(updatedPlayer.getRunsScored());
        existingPlayer.setBalls(updatedPlayer.getBalls());
        existingPlayer.setWickets(updatedPlayer.getWickets());
        existingPlayer.setOvers(updatedPlayer.getOvers());
        existingPlayer.setRunsGiven(updatedPlayer.getRunsGiven());

        return playerRepository.save(existingPlayer);
    }


    public String delete(String firstName) {
        playerRepository.deleteByFirstName(firstName);
        return "Player deleted";
    }

}
