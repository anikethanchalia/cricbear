package com.example.tournament.repository;

import com.example.tournament.model.RegTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RegTeamRepository extends JpaRepository<RegTeam, Integer> {
    @Query("select count(*) from RegTeam where tid = :tid")
    public int countByTid(int tid);
    @Query("select r from RegTeam r where r.groupNumber = :groupNumber and r.tid = :tid")
    public ArrayList<RegTeam> findByGroupNumber(int groupNumber, int tid);
    @Query("select count(*) from RegTeam rt where rt.tid = :tid")
    public int countRegTeamByTid(int tid);


    @Query("select rt from RegTeam rt where rt.tid = :tid and rt.teamid = :teamId")
    RegTeam findByTidAndTeamId(int tid,int teamId);
}
