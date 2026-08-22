package com.starbank.recommendations.service;

import com.starbank.recommendations.config.StaticRules;
import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.dto.RuleStatsDto;
import com.starbank.recommendations.dto.RuleStepDto;
import com.starbank.recommendations.model.ConditionStep;
import com.starbank.recommendations.model.FixedStep;
import com.starbank.recommendations.model.QueryStep;
import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final RuleRepository ruleRepository;

    public Rule createRule(RuleRequestDto dto) {
        Rule rule = new Rule();
        rule.setName(dto.getName());
        rule.setDescription(dto.getDescription());
        rule.setProductId(dto.getProductId());
        rule.setActive(dto.isActive());

        if (dto.getSteps() != null) {
            List<QueryStep> steps = new ArrayList<>();
            for (RuleStepDto stepDto : dto.getSteps()) {
                QueryStep step = new QueryStep();
                step.setStepOrder(stepDto.getStepOrder());
                step.setQueryType(stepDto.getConditionType());
                step.setQueryValue(stepDto.getConditionValue());
                step.setOperator(stepDto.getOperator());
                step.setRule(rule);
                steps.add(step);
            }
            rule.setSteps(steps);
        }

        return ruleRepository.save(rule);
    }

    public List<Rule> getAllActiveRules() {
        return ruleRepository.findAllByIsActiveTrue();
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<Rule> rules;
        try {
            List<Rule> dynamicRules = ruleRepository.findAllByIsActiveTrue();
            rules = new ArrayList<>(StaticRules.STATIC_RULES);
            rules.addAll(dynamicRules);
            log.debug("Recommendations generated with {} dynamic rules and {} static rules for user {}",
                    dynamicRules.size(), StaticRules.STATIC_RULES.size(), userId);
        } catch (DataAccessException e) {
            log.warn("Database unavailable, fallback to static rules only", e);
            rules = new ArrayList<>(StaticRules.STATIC_RULES);
        }

        List<RecommendationDto> recommendations = new ArrayList<>();

        for (Rule rule : rules) {
            if (checkRuleConditions(rule, userId)) {
                RecommendationDto dto = new RecommendationDto();
                dto.setId(rule.getProductId());
                dto.setName(rule.getName());
                dto.setText(rule.getDescription() != null ? rule.getDescription() : "");
                recommendations.add(dto);

                if (rule.getId() != null) {
                    Long current = rule.getApplyCount();
                    rule.setApplyCount(current == null ? 1 : current + 1);
                    ruleRepository.save(rule);
                }
            }
        }

        return recommendations;
    }

    private boolean checkRuleConditions(Rule rule, UUID userId) {

        if (rule.getId() == null) {
            List<FixedStep> steps = StaticRules.getStepsForRule(rule.getName());
            if (steps == null || steps.isEmpty()) {
                return true;
            }
            for (FixedStep step : steps) {
                if (!evaluateCondition(step, userId)) {
                    return false;
                }
            }
            return true;
        }

        List<QueryStep> steps = rule.getSteps();
        if (steps == null || steps.isEmpty()) {
            return true;
        }
        for (QueryStep step : steps) {
            if (!evaluateCondition(step, userId)) {
                return false;
            }
        }
        return true;
    }


    private boolean evaluateCondition(ConditionStep step, UUID userId) {
        String field = step.getQueryType();
        String value = step.getQueryValue();
        String operator = step.getOperator();

        int age = 30;
        long income = 60000;

        switch (field) {
            case "AGE":
                int thresholdAge = Integer.parseInt(value);
                return switch (operator) {
                    case "GT" -> age > thresholdAge;
                    case "GE" -> age >= thresholdAge;
                    case "LT" -> age < thresholdAge;
                    case "LE" -> age <= thresholdAge;
                    default -> false;
                };
            case "INCOME":
                long thresholdIncome = Long.parseLong(value);
                return switch (operator) {
                    case "GT" -> income > thresholdIncome;
                    case "GE" -> income >= thresholdIncome;
                    default -> false;
                };
            default:
                log.warn("Unknown field in rule step: {}", field);
                return false;
        }
    }

    public List<RuleStatsDto> getRuleStats() {
        return ruleRepository.findActiveRulesWithStats();
    }

}
