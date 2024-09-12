package com.example.tournament.service;

import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.PlayerTeam;
import com.example.tournament.repository.PlayerTeamRepository;
import com.example.tournament.service.PlayerTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerTeamServiceTest {

    @Mock
    private PlayerTeamRepository playerTeamRepository;

    @InjectMocks
    private PlayerTeamService playerTeamService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPlayerTeams() {
        // Arrange
        PlayerTeam playerTeam = new PlayerTeam();
        List<PlayerTeam> playerTeams = new ArrayList<>();
        playerTeams.add(playerTeam);
        when(playerTeamRepository.findAll()).thenReturn(playerTeams);

        // Act
        List<PlayerTeam> result = playerTeamService.getAllPlayerTeams();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(playerTeam, result.get(0));
    }

    @Test
    public void testGetPlayerTeamById() {
        // Arrange
        int tpid = 1;
        PlayerTeam playerTeam = new PlayerTeam();
        when(playerTeamRepository.findById(tpid)).thenReturn(Optional.of(playerTeam));

        // Act
        Optional<PlayerTeam> result = playerTeamService.getPlayerTeamById(tpid);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(playerTeam, result.get());
    }

    @Test
    public void testGetPlayerTeamsByTeamId() {
        // Arrange
        int teamId = 1;
        List<Object[]> results = new ArrayList<>();
        // Mock result conversion
        when(playerTeamRepository.findPlayerTeamByTeamId(teamId)).thenReturn(results);

        // Act
        List<PlayerTeamDTO> result = playerTeamService.getPlayerTeamsByTeamId(teamId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSavePlayerTeam() {
        // Arrange
        List<PlayerTeam> playerTeams = new ArrayList<>();
        PlayerTeam playerTeam = new PlayerTeam();
        playerTeam.setTeamId(1);
        playerTeam.setPid(1);
        playerTeam.setPlayerRole(PlayerRole.BATSMAN);
        playerTeam.setOverseas(false);
        playerTeams.add(playerTeam);

        when(playerTeamRepository.countOverseasPlayers(1)).thenReturn(0L);
        when(playerTeamRepository.findByTeamId(1)).thenReturn(new ArrayList<>());

        // Act
        List<PlayerTeam> result = playerTeamService.savePlayerTeam(playerTeams);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(playerTeamRepository, times(1)).save(playerTeam);
    }



    @Test
    public void testDeletePlayerTeam() {
        // Arrange
        int pid = 1;
        when(playerTeamRepository.deleteByPid(pid)).thenReturn(1);

        // Act
        int result = playerTeamService.deletePlayerTeam(pid);

        // Assert
        assertEquals(1, result);
        verify(playerTeamRepository, times(1)).deleteByPid(pid);
    }

    @Test
    public void testCountByTeamId() {
        // Arrange
        int teamId = 1;
        int count = 5;
        when(playerTeamRepository.countByTeamId(teamId)).thenReturn(count);

        // Act
        int result = playerTeamService.countByTeamId(teamId);

        // Assert
        assertEquals(count, result);
    }
}
