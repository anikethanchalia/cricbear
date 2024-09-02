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
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid")
    private int tid;

    @Column(name = "tName")
    private String tName;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "uid")
    private int uid;

    public Tournament(String tName, Date startDate, Date endDate, Status status) {
        this.tName = tName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

}
