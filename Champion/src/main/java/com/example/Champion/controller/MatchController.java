package com.example.Champion.controller;

import com.example.Champion.exception.ResourceNotFoundException;
import com.example.Champion.model.Match;
import com.example.Champion.model.MatchResponse;
import com.example.Champion.model.Participant;
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

    // 1- Get All the matches
    @GetMapping
    public ResponseEntity<List<MatchResponse>> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchResponse> matchResponses = matches.stream()
                .map(MatchResponse::new)  // Convert Match to MatchResponse
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchResponses);
    }
    // 2- Getting Info about matches
    @GetMapping("/info")
    public ResponseEntity<String> matchLimitInfo() {
        return ResponseEntity.ok("You can create up to 3 matches in a day by requesting http://localhost:8081/matches/first-round the 3 matches will created automatically.");
    }

    // 3- Update match winner , results and automatically close the match
    @PutMapping("/{matchId}/result")
    public ResponseEntity<MatchResponse> updateMatchResult(@PathVariable String matchId, @RequestBody Match matchUpdate) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        match.setResult(matchUpdate.getResult());

        if (matchUpdate.getWinnerId() != null) {
            match.setWinnerId(matchUpdate.getWinnerId());
        }

        match.setClosed(true); // Set closed to true when updating the result

        Match updatedMatch = matchRepository.save(match);

        return ResponseEntity.ok(new MatchResponse(updatedMatch));
    }

    // Create first-round matches
    @PostMapping("/first-round")
    public ResponseEntity<?> createFirstRoundMatches() {
        try {
            List<Participant> participants = participantService.getAllParticipants(); 
            if (participants.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: No participants found.");
            }

            String leagueId = participants.get(0).getLeagueId(); 

            matchService.createFirstRoundMatches(leagueId); 
            return ResponseEntity.status(HttpStatus.CREATED).body("First-round matches created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


}
