package com.example.tournament.controller;

import com.example.tournament.model.RegTeam;
import com.example.tournament.service.RegTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regTeam")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class RegTeamController {
    @Autowired
    private RegTeamService regTeamService;

    @PostMapping("/addRegTeam")
    public RegTeam addRegTeam(@RequestBody RegTeam regTeam) {
        return regTeamService.addRegTeam(regTeam);
    }

    @GetMapping("/getAll")
    public List<RegTeam> getAll() {
        return regTeamService.getAllRegTeams();
    }
}
