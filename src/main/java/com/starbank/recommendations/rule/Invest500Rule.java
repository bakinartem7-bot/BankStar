package com.starbank.recommendations.rule;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.repository.RecommendationsRepository;
import com.starbank.recommendations.model.UserAggregates;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommendations.rule.RecommendationConstants.RECOMM_INVEST_500;

@Component
public class Invest500Rule implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public Invest500Rule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        UserAggregates agg = repository.findAggregatesByUserId(userId);

        if (agg.isHasDebit() && !agg.isHasInvest() && agg.getSumSavingDeposits() > 1000) {
            return Optional.of(new RecommendationDto(
                    "Invest 500",
                    "147f6a0f-3b91-413b-ab99-87f081d60d5a",
                    RECOMM_INVEST_500
            ));
        }
        return Optional.empty();
    }
}
