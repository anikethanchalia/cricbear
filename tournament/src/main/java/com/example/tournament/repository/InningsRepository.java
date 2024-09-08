package com.example.tournament.repository;

import com.example.tournament.model.Innings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InningsRepository extends JpaRepository<Innings, Integer> {
    List<Innings> findByMid(Integer mid);

    Innings getByMid(Integer mid);

    Innings getByIid(int iid);
}
