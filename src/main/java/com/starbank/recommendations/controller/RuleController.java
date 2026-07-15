package com.starbank.recommendations.controller;

import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.dto.RuleListResponseDto;
import com.starbank.recommendations.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RecommendationService recommendationService;

    public RuleController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<Void> addRule(@RequestBody RuleRequestDto ruleRequestDto) {
        recommendationService.addRule(ruleRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<RuleListResponseDto> getRules() {
        RuleListResponseDto response = recommendationService.getRules();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        recommendationService.deleteRuleByProductId(productId);
        return ResponseEntity.ok().build();
    }
}
