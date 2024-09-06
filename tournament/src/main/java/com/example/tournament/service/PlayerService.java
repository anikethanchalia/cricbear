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
        if (playerRepository.findByName(player.getName()) == null)
            return playerRepository.save(player);
        return null;
    }

    public List<Player> getAll() {
        return playerRepository.findAll();
    }



    public Player update(int playerId, Player updatedPlayer) {
        // Retrieve the existing player by ID
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        existingPlayer.setName(updatedPlayer.getName());
        existingPlayer.setOverseas(updatedPlayer.isOverseas());
        existingPlayer.setPlayerRole(updatedPlayer.getPlayerRole());


        return playerRepository.save(existingPlayer);
    }


    public String delete(String Name) {
        playerRepository.deleteByName(Name);
        return "Player deleted";
    }

}