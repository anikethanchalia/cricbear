package com.example.tournament.service;

import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.TossDecision;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.PlayerTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.example.tournament.service.BallByBallService.*;

@Service
public class InningsService {

    public static int TOSS_WIN_TEAM;
    public static int BATTING_ID;
    public static int BOWLING_ID;
//    public static int TOSS_DESICION;
    public static TossDecision tossDecision;
    public static List<String> playing11Team1 = new ArrayList<>();
    public static List<String> bowler1;
    public static List<String> playing11Team2 = new ArrayList<>();
    public static List<String> bowler2;
    public static int MID;
    private static final Random RANDOM = new Random();


    @Autowired
    private InningsRepository inningsRepository;
    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerTeamRepository playerTeamRepository;
    @Autowired
    private BallByBallService ballByBallService;

    public Innings create(Innings innings) {
        return inningsRepository.save(innings);
    }

    public List<Innings> getAll() {
        return inningsRepository.findAll();
    }

    public List<Innings> getById(Integer mid) {
        return inningsRepository.findByMid(mid);
    }

    public void delete(Integer iid) {
        inningsRepository.deleteById(iid);
    }

    public Innings update(Innings innings) {
        return inningsRepository.save(innings);
    }

    public static <T extends Enum<T>> T getRandomEnum(Class<T> clazz) {
        List<T> enumValues = Arrays.asList(clazz.getEnumConstants());
        int randomIndex = RANDOM.nextInt(enumValues.size());
        return enumValues.get(randomIndex);
    }

    public void tossDecision(Integer mid) throws InterruptedException {
        MID = mid;
        Match match = matchRepository.getByMid(mid);
        TossDecision decision = getRandomEnum(TossDecision.class);
//        Innings innings = new Innings();
        System.out.println("Toss decision: " + decision);
        if (decision == TossDecision.BAT) {
            TOSS_WIN_TEAM = match.getTeamId1();
            BATTING_ID = match.getTeamId1();
            BOWLING_ID = match.getTeamId2();
            tossDecision = decision;
        }
        else {
            TOSS_WIN_TEAM = match.getTeamId2();
            BATTING_ID = match.getTeamId1();
            BOWLING_ID = match.getTeamId2();
            tossDecision = decision;
        }
        Innings innings = new Innings(mid, BATTING_ID,BOWLING_ID);
        inningsRepository.save(innings);
        System.out.println(tossDecision+" "+BATTING_ID+" "+BOWLING_ID);
        List<String> batsmen1 = playerTeamRepository.findByRoleAndTeam(BATTING_ID, String.valueOf(PlayerRole.BATSMAN),2,true);
        batsmen1.addAll(playerTeamRepository.findByRoleAndTeam(BATTING_ID, String.valueOf(PlayerRole.BATSMAN),2,true));
        List<String> allrounder1 = playerTeamRepository.findByRoleAndTeam(BATTING_ID, String.valueOf(PlayerRole.ALLROUNDER),1,false);
        List<String> wk1 = playerTeamRepository.findByRoleAndTeam(BATTING_ID, String.valueOf(PlayerRole.WICKETKEEPER),1,false);
        bowler1 = playerTeamRepository.findByRoleAndTeam(BATTING_ID, String.valueOf(PlayerRole.BOWLER),1,true);
        bowler1.addAll(playerTeamRepository.findByRoleAndTeam(BATTING_ID, String.valueOf(PlayerRole.BOWLER),4,false));
        playing11Team1.addAll(batsmen1);
        playing11Team1.addAll(allrounder1);
        playing11Team1.addAll(wk1);
        playing11Team1.addAll(bowler1);
        bowler1.addAll(allrounder1);
        System.out.println(playing11Team1);
        List<String> batsmen2 = playerTeamRepository.findByRoleAndTeam(BOWLING_ID, String.valueOf(PlayerRole.BATSMAN),2,false);
        batsmen2.addAll(playerTeamRepository.findByRoleAndTeam(BOWLING_ID, String.valueOf(PlayerRole.BATSMAN),2,true));
        List<String> allrounder2 = playerTeamRepository.findByRoleAndTeam(BOWLING_ID, String.valueOf(PlayerRole.ALLROUNDER),1,false);
        List<String> wk2 = playerTeamRepository.findByRoleAndTeam(BOWLING_ID, String.valueOf(PlayerRole.WICKETKEEPER),1,false);
        bowler2 = playerTeamRepository.findByRoleAndTeam(BOWLING_ID, String.valueOf(PlayerRole.BOWLER),1,true);
        bowler2.addAll(playerTeamRepository.findByRoleAndTeam(BOWLING_ID, String.valueOf(PlayerRole.BOWLER),4,false));
        System.out.println(bowler2);
        System.out.println(bowler1);
        playing11Team2.addAll(batsmen2);
        playing11Team2.addAll(allrounder2);
        playing11Team2.addAll(wk2);
        playing11Team2.addAll(bowler2);
        bowler2.addAll(allrounder2);
        System.out.println(playing11Team2);
//        BallByBallService ballByBallService = new BallByBallService();
        playing11= playing11Team1;
        Innings inn = inningsRepository.getByMid(mid);
        BallByBallService.iid= inn.getIid();
        BallByBallService.bowler= bowler2;
        batter = 0;
        batsman1Id = playing11.get(batter);
        batter++;
        batsman2Id = playing11.get(batter);
        batter++;
        bowlerid = bowler.get(RANDOM.nextInt(bowler.size()));
        bowler.remove(bowlerid);
        enabled=true;
        ballByBallService.startTask1();
//        ballByBallService.startTask2();
        System.out.println("Here");
    }

    public Innings update(int iid, int runs, int wickets, double overToSave) {
        Innings innings = inningsRepository.getByIid(iid);
        System.out.println(innings);
        innings.setRuns(runs);
        innings.setWickets(wickets);
        innings.setOvers(overToSave);
        System.out.println(innings);
        return inningsRepository.save(innings);
    }
}
