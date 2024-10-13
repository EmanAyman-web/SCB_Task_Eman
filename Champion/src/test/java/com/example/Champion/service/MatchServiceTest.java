package com.example.Champion.service;

import com.example.Champion.exception.ResourceNotFoundException;
import com.example.Champion.model.Match;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.MatchRepository;
import com.example.Champion.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughParticipants() {
        // Given
        when(participantRepository.findAll()).thenReturn(Collections.emptyList());

        // When/Then
        assertThrows(RuntimeException.class, () -> matchService.createFirstRoundMatches());
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenMaxMatchesReachedForTheDay() {
        // Given
        Participant participant1 = new Participant();
        Participant participant2 = new Participant();
        List<Participant> participants = Arrays.asList(participant1, participant2);
        when(participantRepository.findAll()).thenReturn(participants);
        when(matchRepository.countByDate(LocalDate.now())).thenReturn(3L);

        // When/Then
        assertThrows(RuntimeException.class, () -> matchService.createFirstRoundMatches());
        verify(matchRepository, times(1)).countByDate(LocalDate.now());
    }

    @Test
    void shouldCreateMatchesSuccessfully() {
        // Given
        Participant participant1 = new Participant();
        participant1.setId("p1");
        Participant participant2 = new Participant();
        participant2.setId("p2");
        List<Participant> participants = Arrays.asList(participant1, participant2);

        when(participantRepository.findAll()).thenReturn(participants);
        when(matchRepository.countByDate(LocalDate.now())).thenReturn(0L);

        // When
        matchService.createFirstRoundMatches();

        // Then
        verify(matchRepository, times(1)).save(any(Match.class));
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenOnlyOneParticipantExists() {
        // Given
        Participant participant1 = new Participant();
        List<Participant> participants = Collections.singletonList(participant1);
        when(participantRepository.findAll()).thenReturn(participants);

        // When/Then
        assertThrows(RuntimeException.class, () -> matchService.createFirstRoundMatches(), "Not enough participants to create matches");
    }

    
   

    @Test
    void shouldThrowExceptionWhenNoParticipants() {
        // Given
        when(participantRepository.findAll()).thenReturn(Collections.emptyList());

        // When/Then
        assertThrows(RuntimeException.class, () -> matchService.createFirstRoundMatches(), "Not enough participants to create matches");
    }

    
    @Test
    void shouldUpdateMatchResultSuccessfully() {
        // Given
        Match match = new Match();
        match.setId("m1");
        match.setResult("draw");
        when(matchRepository.findById("m1")).thenReturn(Optional.of(match));
    
        Match updatedMatch = new Match();
        updatedMatch.setResult("player1 wins"); // The updated result
    
        // When
        matchService.updateMatchResult("m1", updatedMatch); // Pass the updated match object
    
        // Then
        verify(matchRepository, times(1)).save(match);
        assertEquals("player1 wins", match.getResult());
    }
    


    @Test
    void shouldThrowResourceNotFoundWhenMatchNotExists() {
        // Given
        when(matchRepository.findById("1")).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> matchService.updateMatchResult("1", new Match()));
    }
}
