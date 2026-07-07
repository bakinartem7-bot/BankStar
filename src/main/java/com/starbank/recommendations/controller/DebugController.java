package com.starbank.recommendations.controller;

import com.starbank.recommendations.repository.RecommendationsRepository;
import com.starbank.recommendations.model.UserAggregates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DebugController {

    private final RecommendationsRepository repository;

    public DebugController(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/debug/aggregates/{userId}")
    public UserAggregates getAggregates(@PathVariable String userId) {
        return repository.findAggregatesByUserId(UUID.fromString(userId));
    }
}
