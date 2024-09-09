package com.example.tournament.controller;

import com.example.tournament.model.Team;
import com.example.tournament.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<Team> create(@RequestBody Team team) {
        Team createdTeam = teamService.create(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Team> update(@RequestBody Team team) {
        Team updatedTeam = teamService.update(team.getTeamId(), team);
        if (updatedTeam != null) {
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<Team> getByTeamName(@PathVariable String name) {
        Team team = teamService.getByTeamName(name);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Team>> getAll() {
        List<Team> teams = teamService.getAll();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/getByID/{id}")
    public ResponseEntity<Team> getByID(@PathVariable int id) {
        Team team = teamService.getById(id);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        teamService.delete(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @GetMapping("/findInDesc")
    public ResponseEntity<List<Team>> findInDesc() {
        return new ResponseEntity<>(teamService.findAllTeamsInDescendingOrder(), HttpStatus.OK);
    }
}
