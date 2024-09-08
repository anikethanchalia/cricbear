package com.example.tournament.repository;

import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.PlayerTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerTeamRepository extends JpaRepository<PlayerTeam, Integer> {

    @Query("SELECT COUNT(pt) FROM PlayerTeam pt WHERE pt.teamId = :teamId")
    int countByTeamId(@Param("teamId") int teamId);

    @Query("SELECT COUNT(pt) FROM PlayerTeam pt JOIN Player p ON pt.pid = p.pid WHERE pt.teamId = :teamId AND p.overseas = true")
    long countOverseasPlayers(@Param("teamId") int teamId);

    @Query("SELECT COUNT(pt) FROM PlayerTeam pt JOIN Player p ON pt.pid = p.pid WHERE pt.teamId = :teamId AND p.playerRole = :playerRole")
    long countPlayersByRole(@Param("teamId") int teamId, @Param("playerRole") PlayerRole playerRole);

    @Query("SELECT pt FROM PlayerTeam pt JOIN Team t on t.teamId = :teamId")
    List<PlayerTeam> findByTeamId(@Param("teamId") int teamId);

    @Query("SELECT t.teamId,t.teamName ,p.overseas,p.playerRole,p.pid FROM PlayerTeam pt JOIN Team t on pt.teamId = t.teamId, Player p WHERE p.pid = pt.pid")
    List<Object[]> findPlayerTeamByTeamId(@Param("teamId") int teamId);

    @Query(value = "SELECT p.name FROM team_player pt JOIN player_profile p on pt.pid = p.pid WHERE pt.teamId = :teamId AND pt.player_roles = :role and pt.overseas = :overseas LIMIT :limit",nativeQuery = true)
    List<String> findByRoleAndTeam(@Param("teamId") Integer teamId, @Param("role") String role, @Param("limit") int limit,@Param("overseas") boolean overseas);

}