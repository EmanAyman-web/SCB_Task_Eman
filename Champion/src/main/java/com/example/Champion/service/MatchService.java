package com.example.Champion.service;

import com.example.Champion.exception.ResourceNotFoundException;
import com.example.Champion.model.Match;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.MatchRepository;
import com.example.Champion.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ParticipantRepository participantRepository; 

    // 1- createFirstRoundMatches method
    public void createFirstRoundMatches(String leagueId) {
        LocalDate today = LocalDate.now();
        long matchCount = matchRepository.countByDate(today);
        List<Participant> participants = participantRepository.findAll();
        
        if (matchCount >= 3) {
            throw new RuntimeException("Maximum number of matches per day is 3");
        }
    
        if (participants.size() < 2) {
            throw new RuntimeException("Not enough participants to create matches");
        }
    
        Collections.shuffle(participants);
    
        int matchesToCreate = (int) (3 - matchCount);
    
        for (int i = 0; i < matchesToCreate; i++) {
            if (i * 2 + 1 < participants.size()) {
                Participant participantOne = participants.get(i * 2);
                Participant participantTwo = participants.get(i * 2 + 1);
    
                // Create and save a new match
                Match match = new Match();
                match.setParticipantOneId(participantOne.getId());
                match.setParticipantTwoId(participantTwo.getId());
                match.setDate(today); // Ensure this is LocalDate
                match.setLeagueId(leagueId); // Assign the correct leagueId dynamically
                match.setResult("Pending");
                match.setClosed(false);
                matchRepository.save(match);
            } else {
                break; 
            }
        }
    }
    
    
    // 2- getAllMatches method
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    // updateMatchResult method
    public Match updateMatchResult(String matchId, Match matchUpdate) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        match.setResult(matchUpdate.getResult());
        match.setWinnerId(matchUpdate.getWinnerId()); 
        return matchRepository.save(match);
    }

}
