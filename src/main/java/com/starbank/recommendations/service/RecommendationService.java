package com.starbank.recommendations.service;

import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.dto.RuleStepDto;
import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.model.QueryStep;
import com.starbank.recommendations.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
        return new ArrayList<>();
    }
}
