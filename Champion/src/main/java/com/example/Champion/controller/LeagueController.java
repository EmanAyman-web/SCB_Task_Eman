package com.example.Champion.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Champion.service.LeagueService;
import java.util.Map;

@RestController
@RequestMapping("/league")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    // Add new endpoint to submit the league champion
    @PostMapping("/submit-champion")
public ResponseEntity<?> submitLeagueChampion(@RequestBody Map<String, String> requestBody) {
    try {
        String championId = requestBody.get("championId");
        leagueService.submitChampion(championId);
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body("League champion submitted successfully.");
    } catch (Exception e) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
}

}
