package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid")
    private int tid;

    @Column(name = "t_name")
    private String tournamentName;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "uid")
    private int uid;

    public Tournament(String tournamentName, LocalDateTime startDate, LocalDateTime endDate, Status status) {
        this.tournamentName = tournamentName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

}
