package com.example.tournament.controller;

import com.example.tournament.model.Team;
import com.example.tournament.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public Team create(@RequestBody Team team) {
        return teamService.create(team);
    }

    @PutMapping("/update")
    public Team update(@RequestBody Team team) {
        return teamService.update(team.getTeamId(), team);
    }

    @GetMapping("/getByName/{name}")
    public Team getByTeamName(String name) {
        return teamService.getByTeamName(name);
    }

    @GetMapping("/getAll")
    public List<Team> getAll() {
        return teamService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        teamService.delete(id);
        return "Deleted Successfully";
    }
}
