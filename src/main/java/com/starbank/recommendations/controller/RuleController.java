package com.starbank.recommendations.controller;

import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<Rule> createRule(@RequestBody RuleRequestDto dto) {
        Rule rule = recommendationService.createRule(dto);
        return ResponseEntity.ok(rule);
    }

    @GetMapping
    public ResponseEntity<List<Rule>> getActiveRules() {
        List<Rule> rules = recommendationService.getAllActiveRules();
        return ResponseEntity.ok(rules);
    }
}
