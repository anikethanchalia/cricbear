package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_player")
public class PlayerTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tpid")
    private int tpid;

    @Column(name = "pid")
    private int pid;

    @Column(name = "teamid")
    private int teamId;

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

//    // Constructor with all fields except ID
//    public PlayerTeam(Player player, Team team, int runsScored, int balls, int wickets, double overs, int runsGiven) {
//        this.player = player;
//        this.team = team;
//        this.runsScored = runsScored;
//        this.balls = balls;
//        this.wickets = wickets;
//        this.overs = overs;
//        this.runsGiven = runsGiven;
//    }
    public PlayerTeam(int runsScored, int balls,int wickets,double overs,int runsGiven){
        this.runsScored = runsScored;
        this.balls = balls;
        this.wickets = wickets;
        this.overs = overs;
        this.runsGiven = runsGiven;
    }

}
