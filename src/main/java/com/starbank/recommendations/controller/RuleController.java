package com.starbank.recommendations.controller;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.dto.RuleStatsDto;
import com.starbank.recommendations.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/rules")
@Tag(name = "Rule Management", description = "Управление правилами и статистика для менеджера")
@RequiredArgsConstructor
public class RuleController {

    private final RecommendationService recommendationService;
    @PostMapping
    @Operation(summary = "Создать правило", description = "Позволяет менеджеру добавлять новые динамические правила")
    @ApiResponse(responseCode = "201", description = "Правило создано")
    public ResponseEntity<RuleRequestDto> createRule(@RequestBody RuleRequestDto dto) {
        var rule = recommendationService.createRule(dto);
        return ResponseEntity.created(null).body(dto);
    }

    @GetMapping("/stats")
    @Operation(summary = "Статистика правил", description = "Возвращает статистику по активным правилам: количество применений")
    @ApiResponse(responseCode = "200", description = "Список статистики правил")
    public ResponseEntity<List<RuleStatsDto>> getRuleStats() {
        var stats = recommendationService.getRuleStats();
        return ResponseEntity.ok(stats);
    }
}
