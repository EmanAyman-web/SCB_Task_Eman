package com.example.Champion.repository;

import com.example.Champion.model.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {
    @Query("SELECT COUNT(m) FROM Match m WHERE m.date = :date")

    long countByDate(LocalDate date); 

    List<Match> findByDate(LocalDate date); 
}
