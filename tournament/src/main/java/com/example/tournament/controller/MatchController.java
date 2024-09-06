package com.example.tournament.controller;

import com.example.tournament.model.DTO.MatchSemiDTO;
import com.example.tournament.model.Match;
import com.example.tournament.model.Team;
import com.example.tournament.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("/schedule")
    public ArrayList<Match> schedule() {
//        ArrayList<Team> teams = new ArrayList<>();
//
//        teams.add(new Team(1, "Team A", 101, 10, 6, 3, 1, 0, 13, 1.2));
//        teams.add(new Team(2, "Team B", 102, 12, 8, 3, 1, 0, 17, 1.5));
//        teams.add(new Team(3, "Team C", 103, 11, 7, 4, 0, 0, 14, 1.1));
//        teams.add(new Team(4, "Team D", 104, 14, 9, 4, 1, 0, 19, 1.3));
//        teams.add(new Team(5, "Team E", 105, 13, 7, 5, 1, 0, 15, 0.9));
//        teams.add(new Team(6, "Team F", 106, 10, 5, 5, 0, 0, 10, 0.8));
        return matchService.scheduleMatches(1);
    }

    @GetMapping("/semi/{tid}")
    public ArrayList<Match> semi(@PathVariable int tid) {
        System.out.println(tid);
        return matchService.scheduleSemiFinal(tid);
    }
}
