package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.repository.BallByBallRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.tournament.service.InningsService.*;


@Service
@Component
@EnableScheduling
@Data
@AllArgsConstructor
public class BallByBallService {

    @Autowired
    private BallByBallRepository ballByBallRepository;
    
    @Autowired
    private InningsRepository inningsRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchResultService matchResultService;

    @Autowired
    BattingScoreService battingScoreService;


    public Thread taskThread;
    public static ArrayList<Object> possibleOutcomes = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, "W", Extra.WIDE, Extra.NOBALL));
    private static final Random RANDOM = new Random();
    private static final long DELAY = 1000;
    public static boolean enabled = false;
    public static boolean enabled2 = false;
    //    private BallByBall ballByBall;
    public static int target = Integer.MAX_VALUE;
    public static int runs = 0;
    public static int wickets = 0;
    public static int overs = 0;
    public static int ballNumber = 0;
    public static int batter = 0;
    public static List<String> playing11;
    public static List<String> bowler;
    public static int iid;
    public static String batsman1Id;
    public static String batsman2Id;
    public static String bowlerid;
    public static Wicket wicket;
    public static boolean isNoBall;

    @Autowired
    private BowlingScoreService bowlingScoreService;


    public void createBattingScorecard(List<String> playing11Team){
        battingScoreService.create(playing11Team,iid);
    }

    public void createBowlingScorecard(List<String> bowlerTeam){
        bowlingScoreService.create(bowlerTeam,iid);
    }

    public static void setStaticValues() {
        target = runs;
        runs = 0;
        BallByBallService.wickets = 0;
        BallByBallService.overs = 0;
        BallByBallService.ballNumber = 0;
        batter = 0;
        playing11 = playing11Team2;
        BallByBallService.batsman1Id = playing11.get(batter);
        batter++;
        BallByBallService.batsman2Id = playing11.get(batter);
        batter++;
        bowler = bowler1;
        BallByBallService.bowlerid = bowler.get(RANDOM.nextInt(bowler.size()));
        bowler.remove(bowlerid);
    }

    public static <T extends Enum<T>> T getRandomEnum(Class<T> clazz) {
        List<T> enumValues = Arrays.asList(clazz.getEnumConstants());
        int randomIndex = RANDOM.nextInt(enumValues.size());
        return enumValues.get(randomIndex);
    }

    public static void swap() {
        String temp = batsman1Id;
        batsman1Id = batsman2Id;
        batsman2Id = temp;
    }

    public void extraScoreHandler(Extra value) {
        if (Extra.valueOf(value.toString()).equals(Extra.NOBALL)) {
            isNoBall = true;
        }
        runs++;
        bowlingScoreService.update(new BowlingScore(bowlerid, 1L,ballByBallRepository.countByBowlerId(bowlerid,iid),0,iid),iid);
        ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, Extra.valueOf(value.toString()), null, runs, wickets));
    }

    public void wicketHandler(String value) {
        if (!isNoBall) {
            wickets++;
            wicket = getRandomEnum(Wicket.class);
            ballNumber++;
            bowlingScoreService.update(new BowlingScore(bowlerid, 0L,ballByBallRepository.countByBowlerId(bowlerid,iid),1,iid),iid);
            bowlingScoreService.updatePlayerStatsForBowling(new BowlingScore(bowlerid, 0L,ballByBallRepository.countByBowlerId(bowlerid,iid),1,iid));
            battingScoreService.update(new BattingScore(batsman1Id, 0L,1,true,iid),iid);
            battingScoreService.updatePlayerStatsForBatting(battingScoreService.findByName(batsman1Id));
            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, null, wicket, runs, wickets));
            if (wickets == 10) {
                bowlingScoreService.updatePlayerStatsForBowling(bowlingScoreService.findByName(bowlerid));
                battingScoreService.updatePlayerStatsForBatting(battingScoreService.findByName(batsman1Id));
                battingScoreService.updatePlayerStatsForBatting(battingScoreService.findByName(batsman2Id));
                return;
            } else {
                batsman1Id = playing11.get(batter);
                batter++;
            }
        } else {
            ballNumber++;
            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, null, wicket, runs, wickets));
        }
        isNoBall = false;
    }

    public void runsHandler(Integer value) {
        Integer integer = (Integer) value;
        runs += (Integer) value;
        isNoBall = false;
        ballNumber++;
        bowlingScoreService.update(new BowlingScore(bowlerid, value.longValue(), ballByBallRepository.countByBowlerId(bowlerid,iid),0,iid),iid);
        bowlingScoreService.updatePlayerStatsForBowling(new BowlingScore(bowlerid, value.longValue(),ballByBallRepository.countByBowlerId(bowlerid,iid),0,iid));
        battingScoreService.update(new BattingScore(batsman1Id, value.longValue(), 1,false,iid),iid);
        ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, integer, null, null, runs, wickets));
        if (integer % 2 != 0) {
            swap();
        }
    }

    public void overCompleteAndBowlerSelection() {
        if (ballByBallRepository.countByBowlerId(bowlerid, iid) >= 4) {
            bowlerid = null;
        }
        if (bowler.size() <= 2) {
            bowler.addAll(batsmen2);
        }
        overs++;
        ballNumber = 0;
        String temp = bowler.get(RANDOM.nextInt(bowler.size()));
        if (bowlerid != null)
            bowler.add(bowlerid);
        bowlerid = temp;
        bowler.remove(bowlerid);
        swap();
    }

    public void afterFirstInnings(){
        double overToSave = Double.parseDouble(overs+"."+ballNumber);
        Innings innings = new Innings(iid,MID,BATTING_ID,BOWLING_ID, runs, wickets, overToSave);
        inningsRepository.save(innings);
        Innings innings1 = new Innings(MID,BOWLING_ID, BATTING_ID);
        innings1 = inningsRepository.save(innings1);
        iid = innings1.getIid();
        setStaticValues();
        enabled = false;
        enabled2=true;
        stopTask1();
        startTask2();
    }


    public void calculateScore() throws InterruptedException {
//        System.out.println("calculateScore method called");
        if (!enabled) {
            return;
        }
        else {

            if (overs < 20 && wickets < 10) {

                Object value = possibleOutcomes.get(RANDOM.nextInt(possibleOutcomes.size()));
                if(value instanceof Extra) {
                    extraScoreHandler((Extra) value);
                }
                else if (value instanceof String) {
                    if ("W".equals(value)) {
                        wicketHandler(value.toString());
                    }
                }
                else if (value instanceof Integer) {
                    runsHandler((Integer) value);
                }
                if (ballNumber % 6 == 0 && ballNumber != 0) {
                    overCompleteAndBowlerSelection();
                }
            }
            else {
                afterFirstInnings();
            }
        }
    }

    public void startTask1() {
        if (taskThread == null || !taskThread.isAlive()) {
            createBattingScorecard(playing11Team1);
            createBowlingScorecard(bowler);
            taskThread = new Thread(() -> {
                while (enabled) {
                    try {
                        calculateScore();
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupted status
                        // Handle interruption
                    }
                }
            });
            taskThread.start();
        }
    }

    public void stopTask1() {
        enabled = false;
        if (taskThread != null) {
            taskThread.interrupt(); // Interrupt the thread if it is waiting or sleeping
            try {
                taskThread.join(); // Wait for the thread to finish execution
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                // Handle interruption
            }
        }
    }

    public void calculateScoreOfInnings2() throws InterruptedException {
        if (!enabled2) {
            return;
        } else {

            if (overs < 20 && wickets < 10 && target >= 0) {

                Object value = possibleOutcomes.get(RANDOM.nextInt(possibleOutcomes.size()));
                if (value instanceof Extra) {
                    extraScoreHandler((Extra) value);
                    target--;
                } else if (value instanceof String) {
                    if ("W".equals(value)) {
                        wicketHandler(value.toString());
                    }
                } else if (value instanceof Integer) {
                    runsHandler((Integer) value);
                    target -= (Integer) value;
                }
                if (ballNumber % 6 == 0 && ballNumber != 0) {
                    overCompleteAndBowlerSelection();
                }
            }else {
                    double overToSave = Double.parseDouble(overs + "." + ballNumber);
                    Innings innings = new Innings(iid, MID, BOWLING_ID, BATTING_ID, runs, wickets, overToSave);
                    inningsRepository.save(innings);
                    setToNew();
                    teamService.updateAfterResultsForMatch(MID);
                    Team teamBattingSecond = teamService.getById(BOWLING_ID);
                    Team teamBattingFirst = teamService.getById(BATTING_ID);
                    if (target > 0) {
                        double nrr = 0;
                        if (target > 0 && target <= 25)
                            nrr = 0.1;
                        else if (target > 25 && target <= 50)
                            nrr = 0.2;
                        else
                            nrr = 0.3;
                        matchResultService.createMatchResult(new MatchResult(MID, tossDecision, TOSS_WIN_TEAM, teamBattingFirst.getTeamId()));
                        teamService.updateAfterResults(teamBattingFirst, 1, 1, 0, 0, 0, 2, nrr);
                        teamService.updateAfterResults(teamBattingSecond, 1, 0, 1, 0, 0, 0, -nrr);
                    } else if (target < 0) {
                        double nrr = 0;
                        int winByWickets = 10 - wickets;
                        if (winByWickets > 0 && winByWickets <= 3)
                            nrr = 0.1;
                        else if (winByWickets > 3 && winByWickets <= 6)
                            nrr = 0.2;
                        else
                            nrr = 0.3;
                        matchResultService.createMatchResult(new MatchResult(MID, tossDecision, TOSS_WIN_TEAM, teamBattingSecond.getTeamId()));
                        teamService.updateAfterResults(teamBattingSecond, 1, 1, 0, 0, 0, 2, nrr);
                        teamService.updateAfterResults(teamBattingFirst, 1, 0, 1, 0, 0, 0, -nrr);

                    } else {
                        teamService.updateAfterResults(teamBattingSecond, 1, 0, 0, 1, 0, 1, 0);
                        teamService.updateAfterResults(teamBattingFirst, 1, 0, 0, 1, 0, 1, 0);
                    }
                    enabled2 = false;
                }
            }
        }

    public void startTask2() {
        createBattingScorecard(playing11Team2);
        createBowlingScorecard(bowler);
        new Thread(() -> {
            while (enabled2) {
                try {
                    calculateScoreOfInnings2();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    //BallByBall display service
    public Map<Integer, InningsDTO> getBallByBall(int mid){
        List<Innings> innings = inningsRepository.findByMid(mid);
        Map<Integer, InningsDTO> result = new HashMap<>();
        if(innings.size()==0)
            return null;
        String battingTeamName = teamService.getById(innings.getFirst().getBattingId()).getTeamName();
        String bowlingTeamName = teamService.getById(innings.getFirst().getBowlingId()).getTeamName();
        InningsDTO inningsDTO1 = new InningsDTO(mid, battingTeamName, bowlingTeamName, ballByBallRepository.findByIid(innings.getFirst().getIid()));
        result.put(1, inningsDTO1);
        if(innings.size()==1){
            return result;
        }
        battingTeamName = teamService.getById(innings.getLast().getBattingId()).getTeamName();
        bowlingTeamName = teamService.getById(innings.getLast().getBowlingId()).getTeamName();
        InningsDTO inningsDTO2 = new InningsDTO(mid, battingTeamName, bowlingTeamName, ballByBallRepository.findByIid(innings.getLast().getIid()));
        result.put(2, inningsDTO2);
        return result;
    }
}
