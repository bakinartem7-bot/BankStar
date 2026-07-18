package com.starbank.recommendations.service;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.dto.RuleStepDto;
import com.starbank.recommendations.model.ConditionStep;   // <-- используем вынесенный интерфейс
import com.starbank.recommendations.model.QueryStep;
import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RuleRepository ruleRepository;
    private static final List<Rule> FIXED_RULES = List.of(
            createFixedRule("Age > 25", "PROD-A", List.of(new FixedStep("AGE", "25", "GT"))),
            createFixedRule("Income >= 50000", "PROD-B", List.of(new FixedStep("INCOME", "50000", "GE")))
    );

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
        List<RecommendationDto> recommendations = new ArrayList<>();
        List<Rule> allRules = new ArrayList<>(FIXED_RULES);
        allRules.addAll(ruleRepository.findAllByIsActiveTrue());

        for (Rule rule : allRules) {
            if (checkRuleConditions(rule, userId)) {
                RecommendationDto dto = new RecommendationDto();
                dto.setId(rule.getProductId());
                dto.setName(rule.getName());
                dto.setText(rule.getDescription() != null ? rule.getDescription() : "");
                recommendations.add(dto);
            }
        }

        return recommendations;
    }

    private boolean checkRuleConditions(Rule rule, UUID userId) {
        List<? extends ConditionStep> steps;
        if (rule.getId() == null) {
            steps = getFixedStepsForRule(rule.getName());
        } else {
            steps = rule.getSteps();
        }

        if (steps == null || steps.isEmpty()) {
            return true;
        }

        for (ConditionStep step : steps) {
            if (!evaluateCondition(step, userId)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateCondition(ConditionStep step, UUID userId) {
        return true;
    }

    private static Rule createFixedRule(String name, String productId, List<FixedStep> steps) {
        Rule rule = new Rule();
        rule.setName(name);
        rule.setProductId(productId);
        rule.setActive(true);
        return rule;
    }

    private static List<FixedStep> getFixedStepsForRule(String ruleName) {
        if ("Age > 25".equals(ruleName)) {
            return List.of(new FixedStep("AGE", "25", "GT"));
        }
        if ("Income >= 50000".equals(ruleName)) {
            return List.of(new FixedStep("INCOME", "50000", "GE"));
        }
        return Collections.emptyList();
    }

    private static class FixedStep implements ConditionStep {
        private final String queryType;
        private final String queryValue;
        private final String operator;

        FixedStep(String queryType, String queryValue, String operator) {
            this.queryType = queryType;
            this.queryValue = queryValue;
            this.operator = operator;
        }

        @Override
        public String getQueryType() { return queryType; }
        @Override
        public String getQueryValue() { return queryValue; }
        @Override
        public String getOperator() { return operator; }
    }
}
