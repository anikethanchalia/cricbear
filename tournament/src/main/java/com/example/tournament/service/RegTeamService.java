package com.example.tournament.service;

import com.example.tournament.model.RegTeam;
import com.example.tournament.repository.RegTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegTeamService {
    @Autowired
    private RegTeamRepository regTeamRepository;

    public RegTeam addRegTeam(RegTeam regTeam) {
        int count = regTeamRepository.countByTid(regTeam.getTid());
        if(count < 3) {
            regTeam.setGroupNumber(1);
        }
        else
            regTeam.setGroupNumber(2);
        return regTeamRepository.save(regTeam);
    }

    public List<RegTeam> getAllRegTeams() {
        return regTeamRepository.findAll();
    }

    public ArrayList<RegTeam> findByGroupNumber(int groupNumber, int tid) {
        return regTeamRepository.findByGroupNumber(groupNumber,tid);
    }
}
