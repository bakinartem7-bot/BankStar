package com.starbank.recommendations.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FixedStep implements ConditionStep {

    private String queryType;
    private String queryValue;
    private String operator;
}
