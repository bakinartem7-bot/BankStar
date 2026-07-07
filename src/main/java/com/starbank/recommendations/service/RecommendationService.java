package com.starbank.recommendations.service;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> rules;

    public RecommendationService(List<RecommendationRuleSet> rules) {
        this.rules = rules;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> result = new ArrayList<>();
        for (RecommendationRuleSet rule : rules) {
            rule.check(userId).ifPresent(result::add);
        }
        return result;
    }
}
