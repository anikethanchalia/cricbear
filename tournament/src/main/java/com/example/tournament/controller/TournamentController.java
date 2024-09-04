package com.example.tournament.controller;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tournament")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/create")
    public Tournament create(@RequestBody Tournament tournament) {
        return tournamentService.create(tournament);
    }

    @PutMapping("/update")
    public Tournament update(@RequestBody Tournament tournament) {
        return tournamentService.update(tournament);
    }

    @GetMapping("/getById/{id}")
    public Tournament getById(@PathVariable Integer uid) {
        return tournamentService.getById(uid);
    }

    @GetMapping("/getAll")
    public List<Tournament> getAll() {
        return tournamentService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        tournamentService.delete(id);
        return "Deleted Successfully";
    }

    @PostMapping("/getByStatus")
    public List<Tournament> getByStatus(@RequestBody Map<String,String> status) {
        return tournamentService.getByStatus(status.get("status"));
    }
}
