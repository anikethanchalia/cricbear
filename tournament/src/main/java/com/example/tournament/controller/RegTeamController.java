package com.example.tournament.controller;

import com.example.tournament.model.RegTeam;
import com.example.tournament.service.RegTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regTeam")
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
