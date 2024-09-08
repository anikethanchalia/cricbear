package com.example.tournament.controller;

import com.example.tournament.model.Match;
import com.example.tournament.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("/schedule/{tid}")
    public ResponseEntity<ArrayList<Match>> schedule(@PathVariable int tid) {
        try {
            ArrayList<Match> matches = matchService.scheduleMatches(tid);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/semi/{tid}")
    public ResponseEntity<ArrayList<Match>> semi(@PathVariable int tid) {
        try {
            ArrayList<Match> semiFinals = matchService.scheduleSemiFinal(tid);
            return new ResponseEntity<>(semiFinals, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/start/{mid}")
    public ResponseEntity<String> start(@PathVariable int mid) {
        try {
            String result = matchService.startMatch(mid);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
