package com.example.Champion.service;

import com.example.Champion.exception.ResourceNotFoundException;
import com.example.Champion.model.Match;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.MatchRepository;
import com.example.Champion.repository.ParticipantRepository;

// import org.hibernate.engine.internal.Collections;
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
    private ParticipantRepository participantRepository; // Ensure you have this injected for participant checks

    // Method to create the first round matches
    public void createFirstRoundMatches(String leagueId) {
        // Get today's date
        LocalDate today = LocalDate.now();
    
        // Check the number of matches already created for the day
        long matchCount = matchRepository.countByDate(today);
    
        // Prevent match creation if 3 matches already exist for the current day
        if (matchCount >= 3) {
            throw new RuntimeException("Maximum number of matches per day is 3");
        }
    
        // Get all participants
        List<Participant> participants = participantRepository.findAll();
    
        // Check if there are enough participants
        if (participants.size() < 2) {
            throw new RuntimeException("Not enough participants to create matches");
        }
    
        // Shuffle participants to randomize pairing
        Collections.shuffle(participants);
    
        // Create the number of matches needed to reach 3 total matches for the day
        int matchesToCreate = (int) (3 - matchCount);
    
        // Create pairs of participants and save matches
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
                break; // Exit the loop if there are not enough participants for more matches
            }
        }
    }
    
    
    
    // Get all matches
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    // Update match winner and results
    public Match updateMatchResult(String matchId, Match matchUpdate) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        match.setResult(matchUpdate.getResult());
        match.setWinnerId(matchUpdate.getWinnerId()); // Assuming the winner ID is passed
        return matchRepository.save(match);
    }

    // Close a match
    // public Match closeMatch(String matchId) {
    //     Match match = matchRepository.findById(matchId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

    //     match.setClosed(true);
    //     return matchRepository.save(match);
    // }
}
