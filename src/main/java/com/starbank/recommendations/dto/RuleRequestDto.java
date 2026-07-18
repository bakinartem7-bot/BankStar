package com.starbank.recommendations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleRequestDto {

    private String name;
    private String description;
    private String productId;
    private boolean isActive;
    private List<RuleStepDto> steps;
}
