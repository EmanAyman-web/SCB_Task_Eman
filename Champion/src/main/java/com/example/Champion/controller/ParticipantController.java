package com.example.Champion.controller;

import com.example.Champion.model.Participant;
import com.example.Champion.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<?> createParticipant(@RequestBody Participant participant) {
        try {
            Participant createdParticipant = participantService.createParticipant(participant);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipant);
        } catch (IllegalArgumentException e) {
            // Return a structured error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/info")
    public ResponseEntity<String> participantLimitInfo() {
        return ResponseEntity.ok("You can add up to 12 participants.");
    }

    @PostMapping("/group")
    public ResponseEntity<?> randomizeParticipants(@RequestParam int numberOfGroups) {
        // Implement this logic based on your needs
        return ResponseEntity.ok(participantService.randomizeParticipants(numberOfGroups));
    }
}
