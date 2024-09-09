package com.example.tournament.service;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;


    public Tournament create(Tournament tournament) {
        if (tournament.getTournamentName() == null || tournament.getTournamentName().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be null or empty.");
        }

        if (tournamentRepository.findByTournamentName(tournament.getTournamentName()) == null) {
            long duration = ChronoUnit.DAYS.between(tournament.getStartDate(), tournament.getEndDate());
            if (duration >= 12) {
                return tournamentRepository.save(tournament);
            }
            return null;
        }
        return null;
    }


    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }


    public Tournament getById(Integer id) {
        return tournamentRepository.findByUid(id);
    }

    public Tournament getByTid(Integer tid) {
        return tournamentRepository.findByTid(tid);
    }


    public Tournament update(Tournament tournamentDetails,int uid) {
        if (tournamentDetails.getUid()!=uid)
            return null;
        Tournament tournament = tournamentRepository.findByTid(tournamentDetails.getTid());
        tournament.setTournamentName(tournamentDetails.getTournamentName());
        tournament.setStartDate(tournamentDetails.getStartDate());
        tournament.setEndDate(tournamentDetails.getEndDate());
        tournament.setStatus(tournamentDetails.getStatus());
        return tournamentRepository.save(tournament);
    }

    
    public void delete(Integer id) {
        tournamentRepository.deleteById(id);
    }

    public List<Tournament> getByStatus(String status) {
        return tournamentRepository.findAllByStatus(Status.valueOf(status));
    }

    public boolean start(int tid, int uid){
        Tournament tournament = tournamentRepository.findByTid(tid);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);
        System.out.println(formattedDateTime);
        System.out.println(tournament.getStartDate().format(formatter));
        System.out.println(tournament.getStartDate().format(formatter).equals(formattedDateTime));
        if(tournament.getUid() == uid && tournament.getStartDate().format(formatter).equals(formattedDateTime)){
            tournament.setStatus(Status.LIVE);
            tournament = update(tournament,uid);
            return true;
        }
        else {
            return false;
        }
    }
}
