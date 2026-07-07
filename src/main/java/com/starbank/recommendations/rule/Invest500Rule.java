package com.starbank.recommendations.rule;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.repository.RecommendationsRepository;
import com.starbank.recommendations.model.UserAggregates;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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
                    "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!"
            ));
        }
        return Optional.empty();
    }
}
