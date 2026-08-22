package com.starbank.recommendations.model;

public interface ConditionStep {
    String getQueryType();
    String getQueryValue();
    String getOperator();
}
