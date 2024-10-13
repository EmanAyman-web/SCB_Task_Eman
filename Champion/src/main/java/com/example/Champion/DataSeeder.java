package com.example.Champion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.Champion.model.Match;
import com.example.Champion.model.Participant;
import com.example.Champion.repository.MatchRepository;
import com.example.Champion.repository.ParticipantRepository;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public void run(String... args) throws Exception {
        seedParticipants();
        createFirstRoundMatches();
    }

    private void seedParticipants() {
        // Add any initial participants (if needed)
        // Otherwise, keep this method empty if participants are to be added dynamically later
    }

    private void createFirstRoundMatches() {
        matchRepository.deleteAll();

        List<Participant> participants = participantRepository.findAll();
        for (int i = 0; i < participants.size(); i += 2) {
            if (i + 1 < participants.size()) {
                Match match = new Match();
                match.setParticipantOneId(participants.get(i).getId());
                match.setParticipantTwoId(participants.get(i + 1).getId());
                match.setLeagueId(participants.get(i).getLeagueId());
                match.setClosed(false);
                match.setResult("Pending");

                matchRepository.save(match);
            }
        }
    }
}
