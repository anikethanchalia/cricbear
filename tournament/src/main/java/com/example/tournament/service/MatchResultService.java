package com.example.tournament.service;

import com.example.tournament.model.MatchResult;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.MatchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchResultService {
    @Autowired
    private MatchResultRepository matchResultRepository;

    public MatchResult createMatchResult(MatchResult matchResult) {
        return matchResultRepository.save(matchResult);
    }

    public List<MatchResult> getAllMatchResults() {
        return matchResultRepository.findAll();
    }
    public MatchResult getMatchResultById(Integer id) {
        return matchResultRepository.findById(id).orElse(null);
    }
    public MatchResult updateMatchResult(MatchResult matchResult) {
        return matchResultRepository.save(matchResult);
    }
    public void deleteMatchResult(MatchResult matchResult) {
        matchResultRepository.delete(matchResult);
    }
    public MatchResult getMatchResultByMatchId(Integer matchId) {
        return matchResultRepository.findByMid(matchId);
    }
}
