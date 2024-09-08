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
@Table(name = "reg_team")
public class RegTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rtid")
    private int rtid;

    @Column(name = "tid")
    private int tid;

    @Column(name = "teamid")
    private int teamid;

    @Column(name = "group_number")
    private int groupNumber;

    public RegTeam(int tid, int teamid) {
        this.tid = tid;
        this.teamid = teamid;
    }
}
