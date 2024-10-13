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

    // constructor
    public LeagueService(ParticipantRepository participantRepository, ChampionRepository championRepository) {
        this.participantRepository = participantRepository;
        this.championRepository = championRepository;
    }
    
    // submitChampion Method
    public void submitChampion(String championId) {
        Participant champion = participantRepository.findById(championId)
                .orElseThrow(() -> new RuntimeException("Champion not found"));
        Champion championEntity = new Champion();
        championEntity.setParticipantId(championId);
        championEntity.setName(champion.getName());
        championEntity.setWinDate(LocalDate.now());
        championRepository.save(championEntity);
    }
}
