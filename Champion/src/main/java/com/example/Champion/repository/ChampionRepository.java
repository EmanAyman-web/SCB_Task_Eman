package com.example.Champion.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.Champion.model.Champion;

@Repository
public interface ChampionRepository extends MongoRepository<Champion, String> {
    // You can add custom query methods here if needed
}
