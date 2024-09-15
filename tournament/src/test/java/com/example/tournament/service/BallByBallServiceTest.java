package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.repository.BallByBallRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BallByBallServiceTest {

    @InjectMocks
    private BallByBallService ballByBallService;

    @Mock
    private BallByBallRepository ballByBallRepository;
    @Mock
    private InningsRepository inningsRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TeamService teamService;
    @Mock
    private MatchResultService matchResultService;
    @Mock
    private BattingScoreService battingScoreService;
    @Mock
    private BowlingScoreService bowlingScoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BallByBallService.setStaticValues();
    }

    @Test
    void testCreateBattingScorecard() {
        List<String> playing11Team = Arrays.asList("Player1", "Player2", "Player3");
        ballByBallService.createBattingScorecard(playing11Team);
        verify(battingScoreService, times(1)).create(playing11Team, BallByBallService.iid);
    }

    @Test
    void testCreateBowlingScorecard() {
        List<String> bowlerTeam = Arrays.asList("Bowler1", "Bowler2", "Bowler3");
        ballByBallService.createBowlingScorecard(bowlerTeam);
        verify(bowlingScoreService, times(1)).create(bowlerTeam, BallByBallService.iid);
    }

    @Test
    void testSetStaticValues() {
        BallByBallService.runs = 100;
        BallByBallService.setStaticValues();

        assertEquals(100, BallByBallService.target);
        assertEquals(0, BallByBallService.runs);
        assertEquals(0, BallByBallService.wickets);
        assertEquals(0, BallByBallService.overs);
        assertEquals(0, BallByBallService.ballNumber);
        assertNotNull(BallByBallService.batsman1Name);
        assertNotNull(BallByBallService.batsman2Name);
        assertNotNull(BallByBallService.bowlerName);
    }

    @Test
    void testGetRandomEnum() {
        Wicket wicket = BallByBallService.getRandomEnum(Wicket.class);
        assertNotNull(wicket);
        assertTrue(EnumSet.allOf(Wicket.class).contains(wicket));
    }

    @Test
    void testSwap() {
        BallByBallService.batsman1Name = "Batsman1";
        BallByBallService.batsman2Name = "Batsman2";
        BallByBallService.swap();
        assertEquals("Batsman2", BallByBallService.batsman1Name);
        assertEquals("Batsman1", BallByBallService.batsman2Name);
    }

    @Test
    void testUpdateDataAsASingleTransaction() {
        ballByBallService.updateDataAsASingleTransaction(4L, 0, 4, 1, 1, 0, false);
        verify(bowlingScoreService, times(1)).update(anyString(), eq(4L), anyInt(), eq(0), anyInt());
        verify(bowlingScoreService, times(1)).updatePlayerStatsForBowling(anyString(), eq(4), anyInt(), eq(0));
        verify(battingScoreService, times(1)).updatePlayerStatsForBatting(anyString(), eq(4), eq(1), eq(0), eq(0), eq(0));
        verify(battingScoreService, times(1)).update(anyString(), eq(4), eq(1), eq(1), eq(0), eq(false), anyInt());
    }

    @Test
    void testExtraScoreHandler() {
        ballByBallService.extraScoreHandler(Extra.WIDE);
        assertEquals(1, BallByBallService.runs);
        verify(bowlingScoreService, times(1)).update(anyString(), eq(1L), anyInt(), eq(0), anyInt());
        verify(ballByBallRepository, times(1)).save(any(BallByBall.class));
    }

    @Test
    void testWicketHandler() {
        BallByBallService.wickets = 0;
        BallByBallService.ballNumber = 0;
        ballByBallService.wicketHandler();
        assertEquals(1, BallByBallService.wickets);
        assertEquals(1, BallByBallService.ballNumber);
        verify(ballByBallRepository, times(1)).save(any(BallByBall.class));
    }

    @Test
    void testRunsHandler() {
        BallByBallService.runs = 0;
        BallByBallService.ballNumber = 0;
        ballByBallService.runsHandler(4);
        assertEquals(4, BallByBallService.runs);
        assertEquals(1, BallByBallService.ballNumber);
        verify(ballByBallRepository, times(1)).save(any(BallByBall.class));
    }

    @Test
    void testOverCompleteAndBowlerSelection() {
        BallByBallService.overs = 0;
        BallByBallService.ballNumber = 6;
        String initialBowler = BallByBallService.bowlerName;
        ballByBallService.overCompleteAndBowlerSelection();
        assertEquals(1, BallByBallService.overs);
        assertEquals(0, BallByBallService.ballNumber);
        assertNotEquals(initialBowler, BallByBallService.bowlerName);
    }

    @Test
    void testAfterFirstInnings() {
        BallByBallService.runs = 150;
        BallByBallService.wickets = 5;
        BallByBallService.overs = 19;
        BallByBallService.ballNumber = 5;
        ballByBallService.afterFirstInnings();
        verify(inningsRepository, times(2)).save(any(Innings.class));
        assertFalse(BallByBallService.enabled);
        assertTrue(BallByBallService.enabled2);
    }

    @Test
    void testAfterSecondInnings() {
        BallByBallService.target = -1;
        BallByBallService.wickets = 3;
        ballByBallService.afterSecondInnings();
        verify(inningsRepository, times(1)).save(any(Innings.class));
        verify(teamService, times(2)).updateAfterResults(any(Team.class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyDouble());
        verify(matchResultService, times(1)).createMatchResult(any(MatchResult.class));
        assertFalse(BallByBallService.enabled2);
    }

    @Test
    void testCalculateScoreForFirstInnings() throws InterruptedException {
        BallByBallService.enabled = true;
        BallByBallService.overs = 0;
        BallByBallService.wickets = 0;
        ballByBallService.calculateScoreForFirstinnings();
        assertTrue(BallByBallService.runs >= 0);
        assertTrue(BallByBallService.wickets >= 0);
    }

    @Test
    void testCalculateScoreOfInnings2() throws InterruptedException {
        BallByBallService.enabled2 = true;
        BallByBallService.target = 150;
        BallByBallService.overs = 0;
        BallByBallService.wickets = 0;
        ballByBallService.calculateScoreOfInnings2();
        assertTrue(BallByBallService.runs >= 0);
        assertTrue(BallByBallService.wickets >= 0);
        assertTrue(BallByBallService.target <= 150);
    }

    @Test
    void testStartTask1() {
        ballByBallService.startTask1();
        assertNotNull(ballByBallService.taskThread);
        assertTrue(ballByBallService.taskThread.isAlive());
    }

    @Test
    void testStopTask1() {
        ballByBallService.startTask1();
        ballByBallService.stopTask1();
        assertFalse(BallByBallService.enabled);
    }

    @Test
    void testStartTask2() {
        ballByBallService.startTask2();
        assertTrue(BallByBallService.enabled2);
    }

    @Test
    void testStopTask2() {
        ballByBallService.startTask2();
        ballByBallService.stopTask2();
        assertFalse(BallByBallService.enabled2);
    }

    @Test
    void testGetBallByBall() {
        int mid = 1;
        Innings innings1 = new Innings(1, mid, 101, 102, 150, 5, 20.0);
        Innings innings2 = new Innings(2, mid, 102, 101, 145, 10, 19.5);
        List<Innings> inningsList = Arrays.asList(innings1, innings2);

        when(inningsRepository.findByMid(mid)).thenReturn(inningsList);
        when(teamService.getById(101)).thenReturn(new Team("Team1",0,0L,0L,0L,0L,0L,0.0));
        when(teamService.getById(102)).thenReturn(new Team( "Team2",0,0L,0L,0L,0L,0L,0.0));
        when(ballByBallRepository.findByIid(1)).thenReturn(Arrays.asList(new BallByBall()));
        when(ballByBallRepository.findByIid(2)).thenReturn(Arrays.asList(new BallByBall()));

        Map<String, InningsDTO> result = ballByBallService.getBallByBall(mid);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("Innings1"));
        assertTrue(result.containsKey("Innings2"));
        assertEquals("Team1", result.get("Innings1").getBattingTeamName());
        assertEquals("Team2", result.get("Innings1").getBowlingTeamName());
        assertEquals("Team2", result.get("Innings2").getBattingTeamName());
        assertEquals("Team1", result.get("Innings2").getBowlingTeamName());
    }
}