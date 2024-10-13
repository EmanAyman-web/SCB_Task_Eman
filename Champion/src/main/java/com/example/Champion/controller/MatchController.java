package com.example.Champion.controller;

import com.example.Champion.exception.ResourceNotFoundException;
import com.example.Champion.model.Match;
import com.example.Champion.model.MatchResponse;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.ParticipantRepository;
import com.example.Champion.repository.MatchRepository;
import com.example.Champion.service.MatchService;
import com.example.Champion.service.ParticipantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;

    // Get list of all automatically created first-round matches
    @GetMapping
    public ResponseEntity<List<MatchResponse>> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchResponse> matchResponses = matches.stream()
                .map(MatchResponse::new)  // Convert Match to MatchResponse
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchResponses);
    }

    @GetMapping("/info")
    public ResponseEntity<String> matchLimitInfo() {
        return ResponseEntity.ok("You can create up to 3 matches in a day by requesting http://localhost:8081/matches/first-round the 3 matches will created automatically.");
    }

    // Update match winner and results
    @PutMapping("/{matchId}/result")
    public ResponseEntity<MatchResponse> updateMatchResult(@PathVariable String matchId, @RequestBody Match matchUpdate) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        // Update the result of the match
        match.setResult(matchUpdate.getResult());

        // Update the winner ID if provided
        if (matchUpdate.getWinnerId() != null) {
            match.setWinnerId(matchUpdate.getWinnerId());
        }

        // Automatically close the match
        match.setClosed(true); // Set closed to true when updating the result

        // Save the updated match
        Match updatedMatch = matchRepository.save(match);

        return ResponseEntity.ok(new MatchResponse(updatedMatch));
    }

    // Create first-round matches
    @PostMapping("/first-round")
public ResponseEntity<?> createFirstRoundMatches() {
    try {
        // Fetch participants and extract leagueId
        List<Participant> participants = participantService.getAllParticipants(); // Adjust this as necessary
        if (participants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: No participants found.");
        }

        // Assuming leagueId can be derived from the first participant
        String leagueId = participants.get(0).getLeagueId(); // Adjust this as per your data structure

        matchService.createFirstRoundMatches(leagueId); // Pass leagueId to the service
        return ResponseEntity.status(HttpStatus.CREATED).body("First-round matches created successfully.");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
}


}
