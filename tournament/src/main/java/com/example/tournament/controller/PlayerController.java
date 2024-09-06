package com.example.tournament.controller;

import com.example.tournament.model.Player;
import com.example.tournament.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
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


    @GetMapping("/getAll")
    public List<Player> getAll() {
        return playerService.getAll();
    }

    @DeleteMapping("/delete/{name}")
    public String delete(@PathVariable String name) {
        return playerService.delete(name);
    }
}