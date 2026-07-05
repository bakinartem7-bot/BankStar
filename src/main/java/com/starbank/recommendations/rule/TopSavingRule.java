package com.starbank.recommendations.rule;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.repository.RecommendationsRepository;
import com.starbank.recommendations.model.UserAggregates;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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
                    "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем! Преимущества «Копилки»: Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет. Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости. Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг. Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"
            ));
        }
        return Optional.empty();
    }
}
