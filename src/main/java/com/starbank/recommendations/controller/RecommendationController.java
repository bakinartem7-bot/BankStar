
package com.starbank.recommendations.controller;

import com.starbank.recommendations.dto.RecommendationResponse;
import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId) {
        List<com.starbank.recommendations.dto.RecommendationDto> recommendations =
                recommendationService.getRecommendations(userId);

        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendations(recommendations);

        return ResponseEntity.ok(response);
    }
}
