package com.example.tournament.repository;

import com.example.tournament.model.DTO.MatchSemiDTO;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchType;
import com.example.tournament.model.RegTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    public List<Match> findAllByTid(int tournamentId);
    public List<Match> findByMatchType(MatchType matchType);
//    @Query("SELECT m.groupNumber, t.teamId,t.teamName, t.points, t.nrr FROM Match m JOIN RegTeam rt ON :teamId = rt.teamid JOIN Team t ON rt.teamid = t.teamId WHERE m.tid = :tid GROUP BY m.groupNumber ORDER BY t.points DESC")
//    public List<Object[]> getMatchesByTeamId(int tid,int teamid);
//    @Query("SELECT m.mid, m.teamId1, m.teamId2, m.groupNumber, t.points, t.nrr FROM Match m JOIN RegTeam rt ON m.tid = rt.tid JOIN Team t ON rt.teamid = t.teamId WHERE m.tid = :tid GROUP BY m.mid, m.teamId1, m.teamId2, m.groupNumber, t.points, t.nrr ORDER BY t.points DESC")
//    public List<Object[]> getMatchForSemi(int tid);
//    @Query("select ")
    @Query("select r from RegTeam r where r.groupNumber = :groupNumber and r.tid = :tid")
    public ArrayList<RegTeam> findByGroupNumber(int groupNumber, int tid);

    @Query("select t.teamId, rt.groupNumber, t.points,t.nrr from RegTeam rt join Team t on rt.teamid = t.teamId where rt.tid =:tid and rt.groupNumber = :groupNumber order by t.points desc")
    public List<Object[]> getSemiFinal(int tid, int groupNumber);
}
