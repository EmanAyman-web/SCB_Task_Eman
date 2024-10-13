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

    // 1- Create Participants
    @PostMapping
    public ResponseEntity<?> createParticipant(@RequestBody Participant participant) {
        try {
            Participant createdParticipant = participantService.createParticipant(participant);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // 2- Get all The participants from db
    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    // 3- Get info about participants endpoint
    @GetMapping("/info")
    public ResponseEntity<String> participantLimitInfo() {
        return ResponseEntity.ok("You can add up to 12 participants.");
    }

    // 4- group participants into (n) groups in a randomize form
    @PostMapping("/group")
    public ResponseEntity<?> randomizeParticipants(@RequestParam int numberOfGroups) {
        return ResponseEntity.ok(participantService.randomizeParticipants(numberOfGroups));
    }
}
