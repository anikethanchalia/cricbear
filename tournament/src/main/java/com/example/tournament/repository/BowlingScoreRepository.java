package com.example.tournament.repository;

import com.example.tournament.model.BattingScore;
import com.example.tournament.model.BowlingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BowlingScoreRepository extends JpaRepository<BowlingScore, Integer> {

    @Query("select bs from BowlingScore bs where bs.playerName = :playerName and bs.iid = :iid")
    BowlingScore findByNameAndIid(String playerName, Integer iid);

    BowlingScore findByPlayerName(String playerName);

    List<BowlingScore> findByIid(int iid);
}
