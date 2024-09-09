package com.example.tournament.repository;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Tournament findByTid(int id);
    Tournament findByTournamentName(String name);
    Tournament findByUid(int id);
    List<Tournament> findAllByStatus(Status status);
}