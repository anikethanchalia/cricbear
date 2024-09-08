package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.MatchSemiDTO;
import com.example.tournament.model.DTO.MatchSemiDTOComparator;
import com.example.tournament.model.DTO.MatchSemiDTOConverter;
import com.example.tournament.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RegTeamService regTeamService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    InningsService inningsService;

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match findById(Integer id) {
        return matchRepository.findById(id).get();
    }

    public Match save(Match match) {
        return matchRepository.save(match);
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public List<Match> findAllByTid(Integer tid) {
        return matchRepository.findAllByTid(tid);
    }

    public List<Match> findByMatchType(MatchType matchType){
        return matchRepository.findByMatchType(matchType);
    }

    public Match update(Match match) {
        return matchRepository.save(match);
    }

    public String startMatch(int mid) throws InterruptedException {
        Match match = matchRepository.getByMid(mid);
        if(match != null && match.getStatus().equals(MatchStatus.UPCOMING)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = now.format(formatter);
            if (formattedDateTime.equals(formatter.format(match.getMatchDate()))){
                inningsService.startMatch(mid);
                return "Match started";
            }
        }
        return null;
    }
    public ArrayList<Match> scheduleMatches(Integer tid) {
        Tournament t = tournamentService.getByTid(tid);
        ArrayList<Match> matches = new ArrayList<>();
        ArrayList<RegTeam> group1 = regTeamService.findByGroupNumber(1,tid);
        ArrayList<RegTeam> group2 = regTeamService.findByGroupNumber(2,tid);

        System.out.println(group1);
        System.out.println(group2);
        int j = 0, daysToAdd = 0;
        for(int i = 1; i < group1.size() && j < group1.size(); i++){
            if(i!=j){
                Match match = new Match(group1.get(j).getTeamid(),group1.get(i).getTeamid(),t.getStartDate().toLocalDate().plusDays(daysToAdd).atTime(18,00),MatchStatus.UPCOMING,"Chinnaswamy",tid,MatchType.NORMAL);
                matches.add(match);
                daysToAdd++;
                Match match2 = new Match(group2.get(j).getTeamid(),group2.get(i).getTeamid(),t.getStartDate().toLocalDate().plusDays(daysToAdd).atTime(18,00),MatchStatus.UPCOMING,"Chinnaswamy",tid,MatchType.NORMAL);
                matches.add(match2);
                daysToAdd++;
                matchRepository.save(match);
                matchRepository.save(match2);
                System.out.println(match);
                System.out.println(match2);
            }
            if(i==group1.size()-1){
                i=1;
                j++;
            }
        }
        System.out.println(matches);
        return matches;
    }

    public ArrayList<Match> scheduleSemiFinal(Integer tid) {
        Tournament t = tournamentService.getByTid(tid);
        ArrayList<Match> semiMatches = new ArrayList<>();
        List<Object[]> results1 = matchRepository.getSemiFinal(tid,1);
        List<Object[]> results2 = matchRepository.getSemiFinal(tid,2);
        System.out.println(results1);
        System.out.println(results2);
        ArrayList<MatchSemiDTO> matchDTO1 = new ArrayList<>();
        ArrayList<MatchSemiDTO> matchDTO2 = new ArrayList<>();

        for (Object[] result : results1) {
            System.out.println(results1);
            MatchSemiDTO matchDTO = MatchSemiDTOConverter.convert(result);
            matchDTO1.add(matchDTO);
        }
        for (Object[] result : results2) {
            MatchSemiDTO matchDTO = MatchSemiDTOConverter.convert(result);
            matchDTO2.add(matchDTO);
        }

        Collections.sort(matchDTO1, new MatchSemiDTOComparator());
        Collections.sort(matchDTO2, new MatchSemiDTOComparator());
        Match match = new Match(matchDTO1.get(0).getTeamId(),matchDTO2.get(1).getTeamId(),t.getStartDate().toLocalDate().plusDays(8).atTime(18,00),MatchStatus.UPCOMING,"Chinnaswamy",tid,MatchType.SEMIFINAL);
        semiMatches.add(match);
        Match match2 = new Match(matchDTO1.get(1).getTeamId(),matchDTO2.get(0).getTeamId(),t.getStartDate().toLocalDate().plusDays(9).atTime(18,00),MatchStatus.UPCOMING,"Chinnaswamy",tid,MatchType.SEMIFINAL);
        semiMatches.add(match2);
        System.out.println(semiMatches);
//        System.out.println(matchDTO2);
        return semiMatches;
    }
}
