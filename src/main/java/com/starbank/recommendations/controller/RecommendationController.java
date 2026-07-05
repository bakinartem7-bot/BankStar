package com.starbank.recommendations.controller;

import com.starbank.recommendations.dto.RecommendationResponse;
import com.starbank.recommendations.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/recommendation/{user_id}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable("user_id") String userIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        var recommendations = service.getRecommendations(userId);

        RecommendationResponse response = new RecommendationResponse();
        response.setUserId(userIdStr);
        response.setRecommendations(recommendations);

        return ResponseEntity.ok(response);
    }
}
