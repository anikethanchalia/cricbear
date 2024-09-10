package com.example.tournament.service;

import com.example.tournament.model.DTO.MatchResultDTO;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchResult;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.MatchResultRepository;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchResultService {
    @Autowired
    private MatchResultRepository matchResultRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private InningsRepository inningsRepository;

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

    public MatchResultDTO getByMatchId(Integer matchId) {
        MatchResult matches = matchResultRepository.findByMid(matchId);
        List<Innings> innings = inningsRepository.findByMid(matchId);
        Innings firstInning =innings.get(0);
        Innings secondInning =innings.get(1);
        return new MatchResultDTO(matchId,
                matches.getTossDesicion(),
                teamRepository.findByTeamId(matches.getTossWinTeam()).getTeamName(),
                teamRepository.findByTeamId(matches.getWinnerId()).getTeamName(),
                teamRepository.findByTeamId(matchRepository.getByMid(matchId).getTeamId1()).getTeamName(),
                teamRepository.findByTeamId(matchRepository.getByMid(matchId).getTeamId2()).getTeamName(),
                teamRepository.findByTeamId(firstInning.getBattingId()).getTeamName(),
                teamRepository.findByTeamId(secondInning.getBattingId()).getTeamName(),
                firstInning.getRuns(), firstInning.getWickets(), secondInning.getRuns(), secondInning.getWickets());
    }
}
