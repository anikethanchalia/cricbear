package com.example.tournament.service;

import com.example.tournament.model.MatchResult;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.MatchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchResultService {
    @Autowired
    private MatchResultRepository matchResultRepository;

}
