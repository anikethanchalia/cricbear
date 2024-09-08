package com.example.tournament.service;

import com.example.tournament.model.BallByBall;
import com.example.tournament.model.Extra;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Wicket;
import com.example.tournament.repository.BallByBallRepository;
import com.example.tournament.repository.InningsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.example.tournament.service.InningsService.*;


@Service
@Component
@EnableScheduling
@Data
@AllArgsConstructor
public class BallByBallService {

    @Autowired
    private BallByBallRepository ballByBallRepository;
//    @Autowired
//    private InningsService inningsService;

    private Thread taskThread;
    private static ArrayList<Object> possibleOutcomes = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,"W",Extra.WIDE,Extra.NOBALL));
    private static final Random RANDOM = new Random();
    private static final long DELAY = 100;
    public static boolean enabled = false;
    public static boolean enabled2 = false;
    //    private BallByBall ballByBall;
    public static int target = Integer.MAX_VALUE;
    public static int runs = 0;
    public static int wickets = 0;
    public static int overs =0;
    public static int ballNumber = 0;
    public static int batter = 0;
    public static List<String> playing11;
    public static List<String> bowler;
    public static int iid;
    public static String batsman1Id;
    public static String  batsman2Id;
    public static String  bowlerid;
    public static Wicket wicket;
    public static boolean isNoBall;

    @Autowired
    private InningsRepository inningsRepository;




    public static void setStaticValues() {
        target=runs;
        runs=0;
        BallByBallService.wickets = 0;
        BallByBallService.overs = 0;
        BallByBallService.ballNumber = 0;
        batter=0;
        playing11 = playing11Team2;
        BallByBallService.batsman1Id = playing11.get(batter);
        batter++;
        BallByBallService.batsman2Id = playing11.get(batter);
        batter++;
        bowler = bowler2;
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

    public void calculateScore() throws InterruptedException {
//        System.out.println("calculateScore method called");
        if (!enabled) {
            return;
        }
        else {

            if (overs < 20 && wickets < 10) {

                Object value = possibleOutcomes.get(RANDOM.nextInt(possibleOutcomes.size()));
                if (value instanceof Integer) {
                    Integer integer = (Integer) value;
                    runs += (Integer) value;
                    if (integer % 2 != 0) {
                        swap();
                    }
                    isNoBall = false;
                    ballNumber++;
                    ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, integer, null, null, runs, wickets));
                    System.out.println(ballNumber + " " + runs + " " + wickets + " " + batter + " " + bowlerid + " " + batsman1Id + " " + batsman2Id);
                } else if (value instanceof String) {
                    if ("W".equals(value)) {
                        if (!isNoBall) {

                            wickets++;
                            wicket = getRandomEnum(Wicket.class);

                            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, null, wicket, runs, wickets));
                            if (wickets == 10) {
                                return;
                            } else {
                                batsman1Id = playing11.get(batter);
                                batter++;
                            }
                        } else {
                            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, null, wicket, runs, wickets));
                        }
                        isNoBall = false;
                        ballNumber++;
                    }
                    System.out.println(ballNumber + " " + runs + " " + wickets + " " + batter + " " + bowlerid + " " + batsman1Id + " " + batsman2Id);
                } else {
                    if (Extra.valueOf(value.toString()).equals(Extra.NOBALL)) {
                        isNoBall = true;
                    }
                    runs++;
                    ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, Extra.valueOf(value.toString()), null, runs, wickets));
                }
                if (ballNumber % 6 == 0 && ballNumber != 0) {
                    if (ballByBallRepository.countByBowlerId(bowlerid,iid) >= 4) {
                        System.out.println("The Count of " + bowlerid + " is " + ballByBallRepository.countByBowlerId(bowlerid,iid));
                        bowlerid=null;
                        System.out.println(bowler);
                    }
                    overs++;
                    ballNumber = 0;
                    System.out.println("AFTER AN OVER"+bowler);
                    String temp = bowler.get(RANDOM.nextInt(bowler.size()));
                    if (bowlerid!=null)
                        bowler.add(bowlerid);
                    bowlerid = temp;
                    bowler.remove(bowlerid);
                    swap();
                    System.out.println(bowler);
                }
            } else {
                System.out.println("In Else");
                double overToSave = Double.parseDouble(overs+"."+ballNumber);
                System.out.println(ballNumber + " " + overToSave);
                System.out.println(MID);
                Innings innings = new Innings(iid,MID,BATTING_ID,BOWLING_ID, runs, wickets, overToSave);
                System.out.println(innings);
                inningsRepository.save(innings);
                Innings innings1 = new Innings(MID,BOWLING_ID, BATTING_ID);
                innings1 = inningsRepository.save(innings1);
                iid = innings1.getIid();
                System.out.println(iid);
                setStaticValues();
                System.out.println("After setStaticValues");
                System.out.println(ballNumber + " " + runs + " " +target+" "+ wickets + " " + batter + " " + bowlerid + " " + batsman1Id + " " + batsman2Id);
                enabled = false;
                enabled2=true;
                stopTask1();
                startTask2();
            }
        }
    }

    public void startTask1() {
        if (taskThread == null || !taskThread.isAlive()) {
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
        System.out.println("Here");
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

    public void calculateScore2() throws InterruptedException {
//        System.out.println("calculateScore method called");
        if (!enabled2) {
            return;
        }
        else {

            if (overs < 20 && wickets < 10 && target>=0) {

                Object value = possibleOutcomes.get(RANDOM.nextInt(possibleOutcomes.size()));
                if (value instanceof Integer) {
                    Integer integer = (Integer) value;
                    runs += (Integer) value;
                    target-=(Integer) value;
                    if (integer % 2 != 0) {
                        swap();
                    }
                    isNoBall = false;
                    ballNumber++;
                    ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, integer, null, null, runs, wickets));
                    System.out.println(ballNumber + " " + runs + " " + wickets + " " + batter + " " + bowlerid + " " + batsman1Id + " " + batsman2Id);
                } else if (value instanceof String) {
                    if ("W".equals(value)) {
                        if (!isNoBall) {

                            wickets++;
                            wicket = getRandomEnum(Wicket.class);
                            System.out.println(ballNumber + " " + runs + " " + wickets + " " + batter + " " + bowlerid + " " + batsman1Id + " " + batsman2Id+bowler);
                            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, null, wicket, runs, wickets));
                            if (wickets == 10) {
                                return;
                            } else {
                                batsman1Id = playing11.get(batter);
                                batter++;
                            }
                        } else {
                            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, null, wicket, runs, wickets));
                        }
                        isNoBall = false;
                        ballNumber++;
                    }
                    System.out.println(ballNumber + " " + runs + " " + wickets + " " + batter + " " + bowlerid + " " + batsman1Id + " " + batsman2Id+bowler);
                } else {
                    if (Extra.valueOf(value.toString()).equals(Extra.NOBALL)) {
                        isNoBall = true;
                    }
                    runs++;
                    target--;
                    ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Id, batsman2Id, bowlerid, null, Extra.valueOf(value.toString()), null, runs, wickets));
                }
                if (ballNumber % 6 == 0 && ballNumber != 0) {
                    if (ballByBallRepository.countByBowlerId(bowlerid,iid) >= 4) {
                        System.out.println("The Count of " + bowlerid + " is " + ballByBallRepository.countByBowlerId(bowlerid,iid));
                        bowlerid=null;
                        System.out.println(bowler);
                    }
                    overs++;
                    ballNumber = 0;
                    System.out.println("AFTER AN OVER"+bowler);
                    String temp = bowler.get(RANDOM.nextInt(bowler.size()));
                    if (bowlerid!=null)
                        bowler.add(bowlerid);
                    bowlerid = temp;
                    bowler.remove(bowlerid);
                    swap();
                    System.out.println(bowler);
                }
            } else {
                System.out.println("In Else");
                double overToSave = Double.parseDouble(overs+"."+ballNumber);
                System.out.println(ballNumber + " " + overToSave);
                System.out.println(MID);
                Innings innings = new Innings(iid,MID,BOWLING_ID,BATTING_ID, runs, wickets, overToSave);
                System.out.println(innings);
                inningsRepository.save(innings);
                enabled2 = false;
            }
        }
    }
    public void startTask2() {
        new Thread(() -> {
            while (enabled2) {
                try {
                    calculateScore2();
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
        System.out.println("Here");
    }
}
