package com.example.tournament.controller;

import com.example.tournament.model.RegTeam;
import com.example.tournament.service.RegTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regTeam")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class RegTeamController {

    @Autowired
    private RegTeamService regTeamService;

    @PostMapping("/addRegTeam")
    public ResponseEntity<RegTeam> addRegTeam(@RequestBody RegTeam regTeam) {
        if (regTeam == null) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        RegTeam addedRegTeam = regTeamService.addRegTeam(regTeam);
        if (addedRegTeam != null) {
            return new ResponseEntity<>(addedRegTeam, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RegTeam>> getAll() {
        List<RegTeam> regTeams = regTeamService.getAllRegTeams();
        if (regTeams != null && !regTeams.isEmpty()) {
            return new ResponseEntity<>(regTeams, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRegTeam(@RequestBody RegTeam regTeam) {
        if (regTeam == null) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        }
    }

}
