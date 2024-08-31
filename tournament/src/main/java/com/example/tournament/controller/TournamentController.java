package com.example.tournament.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Tournament";
    }
}
