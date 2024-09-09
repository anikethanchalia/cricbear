package com.example.tournament.controller;

import com.example.tournament.model.DTO.MatchDTO;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchStatus;
import com.example.tournament.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("/schedule/{tid}/{uid}")
    public ResponseEntity<ArrayList<Match>> schedule(@PathVariable int tid, @PathVariable int uid) {
        try {
            ArrayList<Match> matches = matchService.scheduleMatches(tid,uid);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/semi/{tid}/{uid}")
    public ResponseEntity<ArrayList<Match>> semiFinal(@PathVariable int tid, @PathVariable int uid) {
        try {
            ArrayList<Match> semiFinals = matchService.scheduleSemiFinal(tid,uid);
            return new ResponseEntity<>(semiFinals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
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

    @GetMapping("/final/{tid}/{uid}")
    public ResponseEntity<Match> finalMatch(@PathVariable int tid, @PathVariable int uid) {
        try {
            Match finalMatch = matchService.finalSchedule(tid,uid);
            return new ResponseEntity<>(finalMatch, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/update/{mid}")
    public ResponseEntity<Match> update(@PathVariable int mid, @RequestBody Match match) {
        try{
            Match updatedMatch = matchService.updateMatch(mid,match);
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/status")
    public ResponseEntity<List<MatchDTO>> status(@RequestBody Map<String, MatchStatus> status) {
        return new ResponseEntity<>(matchService.findByStatus(status.get("status")),HttpStatus.OK);
    }

    @GetMapping("/getByTid/{tid}")
    public ResponseEntity<List<Match>> getByTid(@PathVariable int tid) {
        return new ResponseEntity<>(matchService.findAllByTid(tid),HttpStatus.OK);
    }
}
