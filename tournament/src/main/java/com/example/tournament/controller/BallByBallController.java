package com.example.tournament.controller;

import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.service.BallByBallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ballByBall")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class BallByBallController {

    @Autowired
    private BallByBallService ballByBallService;

    @GetMapping("/{mid}")
    public ResponseEntity<?> getBallByBall(@PathVariable Integer mid) {
        try {
            Map<Integer, InningsDTO> ballByBallData = ballByBallService.getBallByBall(mid);

            if (ballByBallData == null || ballByBallData.isEmpty()) {
                return new ResponseEntity<>("No data found for the provided match ID", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(ballByBallData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while retrieving data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
