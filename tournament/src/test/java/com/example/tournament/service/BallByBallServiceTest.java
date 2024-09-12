package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.repository.*;
import com.example.tournament.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BallByBallServiceTest {

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        BallByBallService.enabled = false;
        BallByBallService.enabled2 = false;
        BallByBallService.runs = 0;
        BallByBallService.wickets = 0;
        BallByBallService.overs = 0;
        BallByBallService.ballNumber = 0;
        BallByBallService.batter = 0;
        BallByBallService.playing11 = new ArrayList<>();
        BallByBallService.bowler = new ArrayList<>();
        BallByBallService.iid = 1;
        BallByBallService.batsman1Id = "batsman1";
        BallByBallService.batsman2Id = "batsman2";
        BallByBallService.bowlerid = "bowler";
        BallByBallService.isNoBall = false;
        BallByBallService.target = Integer.MAX_VALUE;
        BallByBallService.wicket = Wicket.BOWLED;
        BallByBallService.possibleOutcomes = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, "W", Extra.WIDE, Extra.NOBALL));
    }

    @Test
    public void testExtraScoreHandler() {
        // Arrange
        Extra extra = Extra.NOBALL;

        // Act
        ballByBallService.extraScoreHandler(extra);

        // Assert
        verify(ballByBallRepository).save(any(BallByBall.class));
        assertTrue(BallByBallService.isNoBall);
        assertEquals(1, BallByBallService.runs);
    }

    @Test
    public void testWicketHandler() {
        // Arrange
        BallByBallService.playing11 = Arrays.asList("batsman1", "batsman2", "batsman3");
        String value = "W";
        BallByBallService.batsman1Id = "batsman1";
        BallByBallService.batsman2Id = "batsman2";
        BallByBallService.isNoBall = false;

        // Act
        ballByBallService.wicketHandler(value);

        // Assert
        verify(ballByBallRepository).save(any(BallByBall.class));
        assertEquals(1, BallByBallService.wickets);
        assertEquals("batsman1", BallByBallService.batsman1Id);
    }


    @Test
    public void testRunsHandler() {
        // Arrange
        Integer runs = 4;

        // Act
        ballByBallService.runsHandler(runs);

        // Assert
        verify(ballByBallRepository).save(any(BallByBall.class));
        assertEquals(4, BallByBallService.runs);
        assertEquals("batsman2", BallByBallService.batsman2Id);
    }

    @Test
    public void testOverCompleteAndBowlerSelection() {
        // Arrange
        BallByBallService.bowler = new ArrayList<>(Arrays.asList("bowler1", "bowler2", "bowler3"));
        BallByBallService.bowlerid = "bowler1";

        // Act
        ballByBallService.overCompleteAndBowlerSelection();

        // Assert
        assertNotNull(BallByBallService.bowlerid);
        assertNotEquals("bowler2", BallByBallService.bowlerid);
        assertTrue(BallByBallService.bowler.contains("bowler1"));
    }



    @Test
    public void testCalculateScore() throws InterruptedException {
        // Arrange
        BallByBallService.enabled = true;

        // Act
        ballByBallService.calculateScore();

        // Assert
        verify(ballByBallRepository, atLeastOnce()).save(any(BallByBall.class));
    }

    @Test
    public void testStartTask1() throws InterruptedException {
        // Arrange
        BallByBallService.enabled = true;

        // Act
        ballByBallService.startTask1();

        // Assert
        assertNotNull(ballByBallService.taskThread);
        assertTrue(ballByBallService.taskThread.isAlive());
    }

    @Test
    public void testStopTask1() throws InterruptedException {
        // Arrange
        ballByBallService.startTask1();
        Thread.sleep(2000); // Give some time for the task to start

        // Act
        ballByBallService.stopTask1();

        // Assert
        assertFalse(ballByBallService.taskThread.isAlive());
    }



    @Test
    public void testGetBallByBall() {
        // Arrange
        Innings innings = new Innings(1, 1, 2, 3, 100, 5, 10.0);
        when(inningsRepository.findByMid(anyInt())).thenReturn(Collections.singletonList(innings));
        when(ballByBallRepository.findByIid(anyInt())).thenReturn(Collections.emptyList());
        when(teamService.getById(anyInt())).thenReturn(new Team("Team A"));

        // Act
        Map<Integer, InningsDTO> result = ballByBallService.getBallByBall(1);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(1));
        assertNull(result.get(2));
    }
}
