package com.example.Champion.repository;

import com.example.Champion.model.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParticipantRepository extends MongoRepository<Participant, String> {}

