package com.example.tournament.controller;

import com.example.tournament.model.PlayerTeam;
import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.service.PlayerTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/playerTeams")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class PlayerTeamController {

    @Autowired
    private PlayerTeamService playerTeamService;

    // Fetch all player teams (distinct static path)
    @GetMapping("/all")
    public List<PlayerTeam> getAllPlayerTeams() {
        return playerTeamService.getAllPlayerTeams();
    }

    // Fetch player team by ID (dynamic path)
    @GetMapping("/{tpid}")
    public ResponseEntity<PlayerTeam> getPlayerTeamById(@PathVariable int tpid) {
        Optional<PlayerTeam> playerTeam = playerTeamService.getPlayerTeamById(tpid);
        return playerTeam.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Add a new player team (distinct static path)
    @PostMapping("/addPlayerTeam")
    public ResponseEntity<PlayerTeam> addPlayerTeam(@RequestBody PlayerTeamDTO playerTeamDTO) {
        PlayerTeam savedPlayerTeam = playerTeamService.savePlayerTeam(playerTeamDTO);
        return ResponseEntity.ok(savedPlayerTeam);
    }


    // Update an existing player team by ID (dynamic path)
    @PutMapping("/{tpid}")
    public ResponseEntity<PlayerTeam> updatePlayerTeam(@PathVariable int tpid, @RequestBody PlayerTeam updatedPlayerTeam) {
        PlayerTeam updated = playerTeamService.updatePlayerTeam(tpid, updatedPlayerTeam);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a player team by ID (dynamic path)
    @DeleteMapping("/delete/{tpid}")
    public ResponseEntity<Void> deletePlayerTeam(@PathVariable int tpid) {
        playerTeamService.deletePlayerTeam(tpid);
        return ResponseEntity.noContent().build();
    }

    // Count player teams by team ID (distinct static path with dynamic part)
    @GetMapping("/count/team/{id}")
    public int count(@PathVariable int id) {
        return playerTeamService.countByTeamId(id);
    }

    @GetMapping("/team/{teamId}")
    public List<PlayerTeamDTO> getPlayerTeamsByTeamId(@PathVariable int teamId) {
        return playerTeamService.getPlayerTeamsByTeamId(teamId);
    }



}
