package com.starbank.recommendations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.model.QueryStep;
import com.starbank.recommendations.model.RuleEntity;
import com.starbank.recommendations.repository.RuleRepository;
import com.starbank.recommendations.repository.UserKnowledgeRepository;
import com.starbank.recommendations.rule.RecommendationRuleSet; // Путь к твоим старым правилам (Invest500Rule и т.д.)
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> fixedRules; // Старые фиксированные правила
    private final RuleRepository ruleRepository;          // Новые динамические правила из PG
    private final UserKnowledgeRepository knowledgeRepo;   // Репозиторий с кэшами для проверки условий
    private final ObjectMapper objectMapper;              // Для парсинга JSON аргументов

    public RecommendationService(
            List<RecommendationRuleSet> fixedRules,
            RuleRepository ruleRepository,
            UserKnowledgeRepository knowledgeRepo,
            ObjectMapper objectMapper) {
        this.fixedRules = fixedRules;
        this.ruleRepository = ruleRepository;
        this.knowledgeRepo = knowledgeRepo;
        this.objectMapper = objectMapper;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> result = new ArrayList<>();

        for (RecommendationRuleSet rule : fixedRules) {
            Optional<RecommendationDto> dto = rule.check(userId);
            dto.ifPresent(result::add);
        }

        long longUserId = convertUuidToLong(userId);

        List<RuleEntity> dynamicRules = ruleRepository.findAll();
        for (RuleEntity rule : dynamicRules) {
            if (checkDynamicRule(longUserId, rule)) {
                // Формируем DTO для динамического правила
                RecommendationDto dto = new RecommendationDto();
                dto.setProductName(rule.getProductName());
                dto.setProductId(rule.getProductId());
                dto.setText(rule.getProductText());
                result.add(dto);
            }
        }

        return result;
    }

    private boolean checkDynamicRule(long userId, RuleEntity rule) {
        for (QueryStep step : rule.getSteps()) {
            boolean stepResult = evaluateStep(userId, step);

            if (step.isNegate()) {
                stepResult = !stepResult;
            }

            if (!stepResult) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateStep(long userId, QueryStep step) {
        List<String> args;
        try {
            args = objectMapper.readValue(step.getArgumentsJson(), new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный JSON аргументов для шага: " + step.getId(), e);
        }

        switch (step.getQuery()) {
            case "USER_OF":
                return knowledgeRepo.isUserOfProduct(userId, args.get(0));

            case "ACTIVE_USER_OF":
                return knowledgeRepo.isActiveUserOfProduct(userId, args.get(0));

            case "TRANSACTION_SUM_COMPARE":
                return knowledgeRepo.checkTransactionSumCompare(
                        userId,
                        args.get(0),
                        args.get(1),
                        args.get(2),
                        Integer.parseInt(args.get(3))
                );

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                return knowledgeRepo.checkDepositWithdrawCompare(
                        userId,
                        args.get(0),
                        args.get(1)
                );

            default:
                throw new IllegalArgumentException("Неизвестный тип запроса: " + step.getQuery());
        }
    }
// заглушка не могу войти в бд
    private long convertUuidToLong(UUID uuid) {
        return Math.abs(uuid.getMostSignificantBits() % 1000000L);
    }
}
