package com.example.tournament.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matchResult")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class MatchResultController {
}
