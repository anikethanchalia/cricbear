package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private int pid;

    @Column(name = "teamid")
    private int teamid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "overseas")
    private boolean overseas;


    @Column(name = "player_roles")
    @Enumerated(EnumType.STRING)
    private PlayerRole playerRole;

    @Column(name = "runs_scored")
    private int runsScored;

    @Column(name = "balls")
    private int balls;

    @Column(name = "wickets")
    private int wickets;

    @Column(name = "overs")
    private double overs;

    @Column(name = "runs_given")
    private int runsGiven;

}
