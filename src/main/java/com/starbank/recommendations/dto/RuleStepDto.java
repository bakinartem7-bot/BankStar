package com.starbank.recommendations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleStepDto {

    private Integer stepOrder;
    private String conditionType;
    private String conditionValue;
    private String operator;
}
