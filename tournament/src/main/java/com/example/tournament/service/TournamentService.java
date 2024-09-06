package com.example.tournament.service;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;


    public Tournament create(Tournament tournament) {
        if (tournamentRepository.findBytName(tournament.getTName()) == null) {
            if(11 == Math.abs(ChronoUnit.DAYS.between((Temporal) tournament.getEndDate(), (Temporal) tournament.getStartDate()))) {
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


    public Tournament update(Tournament tournamentDetails) {
        Tournament tournament = tournamentRepository.findByTid(tournamentDetails.getTid());
        tournament.setTName(tournamentDetails.getTName());
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
}
