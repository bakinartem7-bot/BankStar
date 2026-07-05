package com.starbank.recommendations.rule;

import com.starbank.recommendations.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> check(UUID userId);
}
