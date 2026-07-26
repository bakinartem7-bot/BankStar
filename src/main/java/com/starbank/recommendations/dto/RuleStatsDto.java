package com.starbank.recommendations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RuleStatsDto {
    private String code;
    private String description;
    private Long applyCount;
}
