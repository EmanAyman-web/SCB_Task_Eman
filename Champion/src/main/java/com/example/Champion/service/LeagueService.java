package com.example.Champion.service;
import org.springframework.stereotype.Service;

import com.example.Champion.model.Champion;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.ChampionRepository;
import com.example.Champion.repository.ParticipantRepository;
import java.time.LocalDate;

@Service
public class LeagueService {

    private final ParticipantRepository participantRepository;
    private final ChampionRepository championRepository;

    public LeagueService(ParticipantRepository participantRepository, ChampionRepository championRepository) {
        this.participantRepository = participantRepository;
        this.championRepository = championRepository;
    }

    // Add method to submit league champion
    public void submitChampion(String championId) {
        // Check if the participant exists
        Participant champion = participantRepository.findById(championId)
                .orElseThrow(() -> new RuntimeException("Champion not found"));

        // Save the champion in a separate collection or mark them as a champion
        Champion championEntity = new Champion();
        championEntity.setParticipantId(championId);
        championEntity.setName(champion.getName());
        championEntity.setWinDate(LocalDate.now());

        // Save the champion to the database
        championRepository.save(championEntity);
    }
}
