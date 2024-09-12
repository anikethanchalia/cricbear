package com.example.tournament.service;

import com.example.tournament.model.BattingScore;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Player;
import com.example.tournament.model.PlayerTeam;
import com.example.tournament.repository.BattingScoreRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BattingScoreService {
    @Autowired
    private BattingScoreRepository battingScoreRepository;
    @Autowired
    private PlayerTeamService playerTeamService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private InningsRepository inningsRepository;

    public List<BattingScore> getAll() {
        return battingScoreRepository.findAll();
    }

    public boolean create(List<String> battingScore, Integer iid) {
        for (String bs : battingScore) {
            battingScoreRepository.save(new BattingScore(bs, 0L,0,false,iid));
        }
        return true;
    }

    public void update(BattingScore battingScore, Integer iid) {
        BattingScore existingBattingScore = battingScoreRepository.findByNameAndIid(battingScore.getPlayerName(),iid);
        if (existingBattingScore!=null) {
            battingScoreRepository.save(new BattingScore(existingBattingScore.getBsid(), battingScore.getPlayerName(), battingScore.getRunsScored()+ existingBattingScore.getRunsScored(),
                    existingBattingScore.getBallsFaced()+ battingScore.getBallsFaced(),battingScore.getIsOut(),iid));
        }
    }

    public BattingScore findByName(String playerName) {
        return battingScoreRepository.findByPlayerName(playerName);
    }

    public void updatePlayerStatsForBatting(BattingScore battingScore) {
        Player player = playerRepository.findByName(battingScore.getPlayerName());
        playerTeamService.updatePlayerTeam(new PlayerTeam(Math.toIntExact(battingScore.getRunsScored()), battingScore.getBallsFaced(),0,0,0),player.getPid());
    }

    public Map<Integer, List<BattingScore>> getAllDataByMid(int mid){
        List<Innings> innings = inningsRepository.findByMid(mid);
        Map<Integer, List<BattingScore>> result = new HashMap<>();
        if(innings.size()==0)
            return null;
        List<BattingScore> battingScores = battingScoreRepository.findByIid(innings.get(0).getIid());
        result.put(1, battingScores);
        if(innings.size()==1){
            return result;
        }
        List<BattingScore> battingScores2 = battingScoreRepository.findByIid(innings.get(1).getIid());
        result.put(2, battingScores2);
        return result;
    }
}
