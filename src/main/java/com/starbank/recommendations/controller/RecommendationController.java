package com.starbank.recommendations.controller;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "Recommendations", description = "Выдача рекомендаций пользователю")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    @Operation(summary = "Получить рекомендации",
            description = "Возвращает список рекомендаций для пользователя с учётом " +
                    "статических и динамических правил. При недоступности БД работает на статических правилах.")
    @ApiResponse(responseCode = "200", description = "Список рекомендаций")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(@PathVariable UUID userId) {
        var recommendations = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }
}

