package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.repository.BallByBallRepository;
import com.example.tournament.repository.BowlingScoreRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BowlingScoreService {
    @Autowired
    private BowlingScoreRepository bowlingScoreRepository;

    @Autowired
    private PlayerTeamService playerTeamService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BallByBallRepository ballByBallRepository;
    @Autowired
    private InningsRepository inningsRepository;

    public List<BowlingScore> getAll() {
        return bowlingScoreRepository.findAll();
    }

    public boolean create(List<String> bowlingScore, Integer iid) {
        for (String bs : bowlingScore) {
            bowlingScoreRepository.save(new BowlingScore(bs, 0L, 0,0, iid));
        }
        return true;
    }

    public void update(BowlingScore bowlingScore, Integer iid) {
        BowlingScore existingBowlingScore = bowlingScoreRepository.findByNameAndIid(bowlingScore.getPlayerName(),iid);
        if (existingBowlingScore !=null) {
            bowlingScoreRepository.save(new BowlingScore(existingBowlingScore.getBsid(), bowlingScore.getPlayerName(), bowlingScore.getRunsGiven()+ existingBowlingScore.getRunsGiven(),
                    existingBowlingScore.getWickets() + bowlingScore.getWickets(),
                     bowlingScore.getOvers(),iid));
        }
    }

    public BowlingScore findByName(String playerName) {
        return bowlingScoreRepository.findByPlayerName(playerName);
    }

    public void updatePlayerStatsForBowling(BowlingScore bowlingScore) {
        Player player = playerRepository.findByName(bowlingScore.getPlayerName());
        playerTeamService.updatePlayerTeam(new PlayerTeam(Math.toIntExact(0), 1, bowlingScore.getWickets(), ballByBallRepository.countByBowlerId(bowlingScore.getPlayerName(), bowlingScore.getIid()), Math.toIntExact(bowlingScore.getRunsGiven())),player.getPid());
    }

    public Map<Integer, List<BowlingScore>> getAllDataByMid(int mid){
        List<Innings> innings = inningsRepository.findByMid(mid);
        Map<Integer, List<BowlingScore>> result = new HashMap<>();
        if(innings.size()==0)
            return null;
        List<BowlingScore> battingScores = bowlingScoreRepository.findByIid(innings.get(0).getIid());
        result.put(1, battingScores);
        if(innings.size()==1){
            return result;
        }
        List<BowlingScore> battingScores2 = bowlingScoreRepository.findByIid(innings.get(1).getIid());
        result.put(2, battingScores2);
        return result;
    }
}

