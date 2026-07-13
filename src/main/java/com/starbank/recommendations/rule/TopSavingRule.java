package com.starbank.recommendations.rule;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.repository.RecommendationsRepository;
import com.starbank.recommendations.model.UserAggregates;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommendations.rule.RecommendationConstants.RECOMM_TOP_SAVING;

@Component
public class TopSavingRule implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public TopSavingRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        UserAggregates agg = repository.findAggregatesByUserId(userId);

        boolean cond1 = agg.isHasDebit();
        boolean cond2 = agg.getSumDebitDeposits() >= 50_000 || agg.getSumSavingDeposits() >= 50_000;
        boolean cond3 = agg.getSumDebitDeposits() > agg.getSumDebitWithdrawals();

        if (cond1 && cond2 && cond3) {
            return Optional.of(new RecommendationDto(
                    "Top Saving",
                    "59efc529-2fff-41af-baff-90ccd7402925",
                    RECOMM_TOP_SAVING
            ));
        }
        return Optional.empty();
    }
}
