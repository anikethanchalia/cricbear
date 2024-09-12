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
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamid")
    private int teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "coachid")
    private int coachId;

    @Column(name = "matches_played")
    private long matchesPlayed;

    @Column(name = "matches_won")
    private long matchesWon;

    @Column(name = "matches_lost")
    private long matchesLost;

    @Column(name = "matches_drawn")
    private long matchesDrawn;

    @Column(name = "matches_abandoned")
    private long matchesAbandoned;

    @Column(name = "points")
    private int points;

    @Column(name = "nrr")
    private double nrr;

    public Team(String teamName, int coachId, long matchesPlayed,long matchesWon, long matchesLost, long matchesDrawn, long matchesAbandoned, double nrr ) {
        this.teamName = teamName;
        this.coachId = coachId;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesLost = matchesLost;
        this.matchesDrawn = matchesDrawn;
        this.matchesAbandoned = matchesAbandoned;
        this.nrr = nrr;
    }

    public Team(String teamA) {
    }
}
