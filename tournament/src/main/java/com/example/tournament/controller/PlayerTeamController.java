package com.example.tournament.controller;

import com.example.tournament.model.PlayerTeam;
import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.service.PlayerTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playerTeams")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PlayerTeamController {

    @Autowired
    private PlayerTeamService playerTeamService;

    // Fetch all player teams
    @GetMapping("/all")
    public ResponseEntity<List<PlayerTeam>> getAllPlayerTeams() {
        List<PlayerTeam> playerTeams = playerTeamService.getAllPlayerTeams();
        if (playerTeams != null && !playerTeams.isEmpty()) {
            return new ResponseEntity<>(playerTeams, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
    }

    // Fetch player team by ID
    @GetMapping("/{tpid}")
    public ResponseEntity<PlayerTeam> getPlayerTeamById(@PathVariable int tpid) {
        Optional<PlayerTeam> playerTeam = playerTeamService.getPlayerTeamById(tpid);
        return playerTeam.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new player team
    @PostMapping("/addPlayerTeam")
    public ResponseEntity<PlayerTeam> addPlayerTeam(@RequestBody PlayerTeamDTO playerTeamDTO) {
        if (playerTeamDTO == null) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        PlayerTeam savedPlayerTeam = playerTeamService.savePlayerTeam(playerTeamDTO);
        if (savedPlayerTeam != null) {
            return new ResponseEntity<>(savedPlayerTeam, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing player team by ID
    @PutMapping("/{tpid}")
    public ResponseEntity<PlayerTeam> updatePlayerTeam(@PathVariable int tpid, @RequestBody PlayerTeam updatedPlayerTeam) {
        PlayerTeam updated = playerTeamService.updatePlayerTeam(tpid, updatedPlayerTeam);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    // Delete a player team by ID
    @DeleteMapping("/delete/{tpid}")
    public ResponseEntity<Void> deletePlayerTeam(@PathVariable int tpid) {
        playerTeamService.deletePlayerTeam(tpid);
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    // Count player teams by team ID
    @GetMapping("/count/team/{id}")
    public ResponseEntity<Integer> count(@PathVariable int id) {
        int count = playerTeamService.countByTeamId(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Get player teams by team ID
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerTeamDTO>> getPlayerTeamsByTeamId(@PathVariable int teamId) {
        List<PlayerTeamDTO> playerTeams = playerTeamService.getPlayerTeamsByTeamId(teamId);
        if (playerTeams != null && !playerTeams.isEmpty()) {
            return new ResponseEntity<>(playerTeams, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
    }
}
