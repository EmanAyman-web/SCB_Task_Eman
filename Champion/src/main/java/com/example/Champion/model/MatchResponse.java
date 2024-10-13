package com.example.Champion.model; // Ensure this package is correct

import java.time.LocalDate; // Import LocalDate instead of java.sql.Date

public class MatchResponse {
    private String id;
    private String participantOneId;
    private String participantTwoId;
    private String winnerId;
    private String leagueId;
    private String result;
    private boolean closed;
    private LocalDate date; // Change to LocalDate

    public MatchResponse(Match match) {
        this.id = match.getId();
        this.participantOneId = match.getParticipantOneId();
        this.participantTwoId = match.getParticipantTwoId();
        this.winnerId = match.getWinnerId();
        this.leagueId = match.getLeagueId();
        this.result = match.getResult();
        this.closed = match.isClosed();
        this.date = match.getDate(); // Now matches the type LocalDate
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getParticipantOneId() {
        return participantOneId;
    }

    public String getParticipantTwoId() {
        return participantTwoId;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public String getResult() {
        return result;
    }

    public boolean isClosed() {
        return closed;
    }

    public LocalDate getDate() {  // Change the return type to LocalDate
        return date;
    }
}
