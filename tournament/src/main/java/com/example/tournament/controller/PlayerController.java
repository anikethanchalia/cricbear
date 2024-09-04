package com.example.tournament.controller;

import com.example.tournament.model.Player;
import com.example.tournament.model.Team;
import com.example.tournament.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "http://localhost:3000")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/create")
    public Player create(@RequestBody Player player) {
        return playerService.create(player);
    }

    @PutMapping("/update")
    public Player update(@RequestBody Player player) {
        return playerService.update(player.getPid(), player);
    }

    @GetMapping("/getByName/{id}")
    public List<Player> getByTeamName(@PathVariable Integer id) {
        return playerService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Player> getAll() {
        return playerService.getAll();
    }

    @DeleteMapping("/delete/{first_name}")
    public String delete(@PathVariable String first_name) {
        return playerService.delete(first_name);
    }
}
