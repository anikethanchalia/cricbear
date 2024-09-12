package com.example.tournament.service;

import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.TossDecision;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.PlayerTeamRepository;
import com.example.tournament.service.BallByBallService;
import com.example.tournament.service.InningsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InningsServiceTest {

    @Mock
    private InningsRepository inningsRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerTeamRepository playerTeamRepository;

    @Mock
    private BallByBallService ballByBallService;

    @InjectMocks
    private InningsService inningsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        // Arrange
        Innings innings = new Innings();
        when(inningsRepository.save(innings)).thenReturn(innings);

        // Act
        Innings result = inningsService.create(innings);

        // Assert
        assertEquals(innings, result);
        verify(inningsRepository).save(innings);
    }

    @Test
    public void testGetAll() {
        // Arrange
        List<Innings> inningsList = new ArrayList<>();
        when(inningsRepository.findAll()).thenReturn(inningsList);

        // Act
        List<Innings> result = inningsService.getAll();

        // Assert
        assertEquals(inningsList, result);
        verify(inningsRepository).findAll();
    }

    @Test
    public void testGetById() {
        // Arrange
        Integer mid = 1;
        List<Innings> inningsList = new ArrayList<>();
        when(inningsRepository.findByMid(mid)).thenReturn(inningsList);

        // Act
        List<Innings> result = inningsService.getById(mid);

        // Assert
        assertEquals(inningsList, result);
        verify(inningsRepository).findByMid(mid);
    }

    @Test
    public void testDelete() {
        // Arrange
        Integer iid = 1;

        // Act
        inningsService.delete(iid);

        // Assert
        verify(inningsRepository).deleteById(iid);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Innings innings = new Innings();
        innings.setIid(1);
        innings.setRuns(100);
        innings.setWickets(5);
        innings.setOvers(10.5);
        when(inningsRepository.getByIid(1)).thenReturn(innings);
        when(inningsRepository.save(innings)).thenReturn(innings);

        // Act
        Innings result = inningsService.update(1, 100, 5, 10.5);

        // Assert
        assertEquals(innings, result);
        verify(inningsRepository).getByIid(1);
        verify(inningsRepository).save(innings);
    }

}
