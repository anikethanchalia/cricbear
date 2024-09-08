package com.example.tournament.controller;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.MatchService;
import com.example.tournament.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tournament")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private MatchService matchService;

    @PostMapping("/create")
    public ResponseEntity<Tournament> create(@RequestBody Tournament tournament) {
        Tournament createdTournament = tournamentService.create(tournament);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Tournament> update(@RequestBody Tournament tournament) {
        Tournament updatedTournament = tournamentService.update(tournament);
        if (updatedTournament != null) {
            return new ResponseEntity<>(updatedTournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Tournament> getById(@PathVariable Integer id) {
        Tournament tournament = tournamentService.getById(id);
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Tournament>> getAll() {
        List<Tournament> tournaments = tournamentService.getAll();
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        tournamentService.delete(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PostMapping("/getByStatus")
    public ResponseEntity<List<Tournament>> getByStatus(@RequestBody Map<String, String> status) {
        List<Tournament> tournaments = tournamentService.getByStatus(status.get("status"));
        if (tournaments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(tournaments, HttpStatus.OK);
        }
    }

    @PostMapping("/start")
    public ResponseEntity<ArrayList<Match>> start(@RequestBody Map<String, Integer> tournament) {
        boolean success = tournamentService.start(tournament.get("tid"), tournament.get("uid"));
        if (success) {
            return new ResponseEntity<>(matchService.scheduleMatches(tournament.get("tid")), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
}
