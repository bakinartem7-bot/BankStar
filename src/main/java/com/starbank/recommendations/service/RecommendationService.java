package com.starbank.recommendations.service;

import com.starbank.recommendations.dto.RecommendationDto;
import com.starbank.recommendations.dto.RuleRequestDto;
import com.starbank.recommendations.dto.RuleListResponseDto;
import com.starbank.recommendations.dto.RuleResponseDto;
import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.repository.RuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RuleRepository ruleRepository;

    public RecommendationService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Transactional("ruleTransactionManager")
    public void addRule(RuleRequestDto ruleRequestDto) {
        Rule rule = ruleRequestDto.toRule();
        ruleRepository.save(rule);
    }

    @Transactional(readOnly = true, transactionManager = "ruleTransactionManager")
    public RuleListResponseDto getRules() {
        List<Rule> rules = ruleRepository.findAll();

        List<RuleResponseDto> dtoList = rules.stream()
                .map(this::mapRuleToDto)
                .collect(Collectors.toList());

        return new RuleListResponseDto(dtoList);
    }

    @Transactional("ruleTransactionManager")
    public void deleteRuleByProductId(String productId) {
        ruleRepository.deleteByProductId(productId);
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        return List.of();
    }
    private RuleResponseDto mapRuleToDto(Rule rule) {
        RuleResponseDto dto = new RuleResponseDto();
        dto.setId(rule.getId());
        dto.setProductName(rule.getProductName());
        dto.setProductId(rule.getProductId());
        dto.setProductText(rule.getProductText());
        return dto;
    }
}
