package com.example.tournament.model.DTO;

import com.example.tournament.model.MatchStatus;
import com.example.tournament.model.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    int mid;
    int tid;
    String team1;
    String team2;
    LocalDateTime matchDate;
    String stadium;
    MatchStatus status;
    MatchType type;

    public MatchDTO(int mid, int tid, String teamName, String teamName1, MatchStatus status, MatchType matchType) {
        this.mid = mid;
        this.tid = tid;
        this.team1 = teamName;
        this.team2 = teamName1;
        this.status = status;
        this.matchDate = LocalDateTime.now();
        this.type = matchType;
    }
}
