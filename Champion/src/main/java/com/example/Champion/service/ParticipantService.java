package com.example.Champion.service;

import com.example.Champion.model.Participant;
import com.example.Champion.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    // 1- createParticipant method
    public Participant createParticipant(Participant participant) {
        long participantCount = participantRepository.count();
        if (participantCount >= 12) {
            throw new IllegalArgumentException("Maximum number of participants is 12");
        }
        return participantRepository.save(participant);
    }

    // 2- getAllParticipants method
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    // 3- randomizeParticipants method
    public Map<String, List<Participant>> randomizeParticipants(int numberOfGroups) {
        List<Participant> participants = participantRepository.findAll();
        Collections.shuffle(participants);

        Map<String, List<Participant>> groups = new java.util.HashMap<>();
        int groupSize = participants.size() / numberOfGroups;
        
        for (int i = 0; i < numberOfGroups; i++) {
            groups.put("Group " + (i + 1), participants.subList(i * groupSize, Math.min((i + 1) * groupSize, participants.size())));
        }

        return groups;
    }
}
