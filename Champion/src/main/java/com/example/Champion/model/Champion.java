package com.example.Champion.model;

import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Document(collection = "champions")
public class Champion {

    @Id
    private String id;
    private String participantId;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate winDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getWinDate() {
        return winDate;
    }

    public void setWinDate(LocalDate winDate) {
        this.winDate = winDate;
    }
}
