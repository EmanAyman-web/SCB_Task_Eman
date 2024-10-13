package com.example.Champion.service;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map; 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// unit test for participants
class ParticipantServiceTest {

    @InjectMocks
    private ParticipantService participantService;

    @Mock
    private ParticipantRepository participantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateParticipantSuccess() {
        Participant participant = new Participant();
        when(participantRepository.count()).thenReturn(0L);
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        Participant createdParticipant = participantService.createParticipant(participant);

        assertNotNull(createdParticipant);
        verify(participantRepository).save(participant);
    }

    @Test
    void testCreateParticipantExceedsLimit() {
        when(participantRepository.count()).thenReturn(12L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            participantService.createParticipant(new Participant());
        });

        assertEquals("Maximum number of participants is 12", exception.getMessage());
        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    void testGetAllParticipants() {
        Participant participant1 = new Participant();
        Participant participant2 = new Participant();
        when(participantRepository.findAll()).thenReturn(Arrays.asList(participant1, participant2));

        List<Participant> participants = participantService.getAllParticipants();

        assertEquals(2, participants.size());
        verify(participantRepository).findAll();
    }

    @Test
    void testRandomizeParticipants() {
        Participant participant1 = new Participant();
        Participant participant2 = new Participant();
        Participant participant3 = new Participant();
        when(participantRepository.findAll()).thenReturn(Arrays.asList(participant1, participant2, participant3));

        Map<String, List<Participant>> groups = participantService.randomizeParticipants(2);

        assertEquals(2, groups.size());
        assertTrue(groups.get("Group 1").size() <= 2); // Group size should not exceed total participants
        assertTrue(groups.get("Group 2").size() <= 2);
        verify(participantRepository).findAll();
    }

    @Test
    void testRandomizeParticipantsNoGroups() {
        when(participantRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, List<Participant>> groups = participantService.randomizeParticipants(2);

        assertEquals(2, groups.size());
        assertTrue(groups.get("Group 1").isEmpty());
        assertTrue(groups.get("Group 2").isEmpty());
    }
}
