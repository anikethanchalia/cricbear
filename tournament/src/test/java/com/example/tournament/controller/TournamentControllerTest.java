package com.example.tournament.controller;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TournamentController.class)
class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    private MockMvc mockMvc;

    private Tournament tournament;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController).build();

        tournament = new Tournament();
        tournament.setTid(1);
        tournament.setTName("Champions Trophy");
        tournament.setStartDate(Date.valueOf("2024-09-01"));
        tournament.setEndDate(Date.valueOf("2024-09-10"));
        tournament.setStatus(Status.UPCOMING);
    }

    @Test
    void create_ShouldReturnCreatedTournament() throws Exception {
        when(tournamentService.create(any(Tournament.class))).thenReturn(tournament);

        mockMvc.perform(post("/tournament/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tournament)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tName").value("Champions Trophy"))
                .andExpect(jsonPath("$.status").value("UPCOMING"));
    }

    @Test
    void update_ShouldReturnUpdatedTournament() throws Exception {
        when(tournamentService.update(any(Tournament.class))).thenReturn(tournament);

        mockMvc.perform(put("/tournament/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tournament)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tName").value("Champions Trophy"))
                .andExpect(jsonPath("$.status").value("UPCOMING"));
    }

    @Test
    void getById_ShouldReturnTournament() throws Exception {
        when(tournamentService.getById(1)).thenReturn(tournament);

        mockMvc.perform(get("/tournament/getById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tName").value("Champions Trophy"))
                .andExpect(jsonPath("$.status").value("UPCOMING"));
    }

    @Test
    void getAll_ShouldReturnAllTournaments() throws Exception {
        List<Tournament> tournaments = Arrays.asList(tournament);
        when(tournamentService.getAll()).thenReturn(tournaments);

        mockMvc.perform(get("/tournament/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tName").value("Champions Trophy"));
    }

    @Test
    void delete_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/tournament/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));
    }

    @Test
    void getByStatus_ShouldReturnTournamentsWithStatus() throws Exception {
        List<Tournament> tournaments = Arrays.asList(tournament);
        when(tournamentService.getByStatus("UPCOMING")).thenReturn(tournaments);

        mockMvc.perform(post("/tournament/getByStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("status", "UPCOMING"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tName").value("Champions Trophy"))
                .andExpect(jsonPath("$[0].status").value("UPCOMING"));
    }
}