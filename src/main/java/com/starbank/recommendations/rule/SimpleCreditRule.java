package com.starbank.recommendations.rule;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.repository.RecommendationsRepository;
import com.starbank.recommendations.model.UserAggregates;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommendations.rule.RecommendationConstants.RECOMM_SIMPLE_CREDIT;

@Component
public class SimpleCreditRule implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public SimpleCreditRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        UserAggregates agg = repository.findAggregatesByUserId(userId);

        boolean noCredit = !agg.isHasCredit();
        boolean depositMoreThanWithdrawal = agg.getSumDebitDeposits() > agg.getSumDebitWithdrawals();
        boolean withdrawalMoreThan100k = agg.getSumDebitWithdrawals() > 100_000;

        if (noCredit && depositMoreThanWithdrawal && withdrawalMoreThan100k) {
            return Optional.of(new RecommendationDto(
                    "Простой кредит",
                    "ab138afb-f3ba-4a93-b74f-0fcee86d447f",
                    RECOMM_SIMPLE_CREDIT
            ));
        }
        return Optional.empty();
    }
}
