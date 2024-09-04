package com.example.tournament.service;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentService tournamentService;

    private Tournament tournament;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = new Tournament();
        tournament.setTid(1);
        tournament.setTName("Champions Trophy");
        tournament.setStartDate(Date.valueOf("2024-09-01"));  // Convert String to java.sql.Date
        tournament.setEndDate(Date.valueOf("2024-09-10"));    // Convert String to java.sql.Date
        tournament.setStatus(Status.UPCOMING);
    }


    @Test
    void create_WhenTournamentDoesNotExist_ShouldCreateTournament() {
        when(tournamentRepository.findBytName(tournament.getTName())).thenReturn(null);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament createdTournament = tournamentService.create(tournament);

        assertNotNull(createdTournament);
        assertEquals("Champions Trophy", createdTournament.getTName());
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void create_WhenTournamentAlreadyExists_ShouldReturnNull() {
        when(tournamentRepository.findBytName(tournament.getTName())).thenReturn(tournament);

        Tournament createdTournament = tournamentService.create(tournament);

        assertNull(createdTournament);
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    void getAll_ShouldReturnAllTournaments() {
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament));

        List<Tournament> tournaments = tournamentService.getAll();

        assertEquals(1, tournaments.size());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnTournament() {
        when(tournamentRepository.findByUid(1)).thenReturn(tournament);

        Tournament foundTournament = tournamentService.getById(1);

        assertNotNull(foundTournament);
        assertEquals(1, foundTournament.getTid());
        verify(tournamentRepository, times(1)).findByUid(1);
    }

    @Test
    void update_ShouldUpdateAndReturnTournament() {
        Tournament updatedDetails = new Tournament();
        updatedDetails.setTid(1);
        updatedDetails.setTName("Updated Trophy");
        updatedDetails.setStartDate(Date.valueOf("2024-09-02"));  // Convert String to java.sql.Date
        updatedDetails.setEndDate(Date.valueOf("2024-09-11"));    // Convert String to java.sql.Date
        updatedDetails.setStatus(Status.LIVE);


        when(tournamentRepository.findByTid(1)).thenReturn(tournament);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(updatedDetails);

        Tournament updatedTournament = tournamentService.update(updatedDetails);

        assertNotNull(updatedTournament);
        assertEquals("Updated Trophy", updatedTournament.getTName());
        assertEquals(Status.LIVE, updatedTournament.getStatus());
        verify(tournamentRepository, times(1)).save(updatedTournament);
    }

    @Test
    void delete_ShouldDeleteTournamentById() {
        doNothing().when(tournamentRepository).deleteById(1);

        tournamentService.delete(1);

        verify(tournamentRepository, times(1)).deleteById(1);
    }

    @Test
    void getByStatus_ShouldReturnTournamentsWithStatus() {
        when(tournamentRepository.findAllByStatus(Status.UPCOMING)).thenReturn(Arrays.asList(tournament));

        List<Tournament> tournaments = tournamentService.getByStatus("UPCOMING");

        assertEquals(1, tournaments.size());
        assertEquals(Status.UPCOMING, tournaments.get(0).getStatus());
        verify(tournamentRepository, times(1)).findAllByStatus(Status.UPCOMING);
    }
}






