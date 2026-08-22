package com.starbank.recommendations.dto;

import java.util.List;

public class RuleResponseDto {
    private Long id;
    private String productName;
    private String productId;
    private String productText;
    private List<RuleStepDto> rule;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }
    public List<RuleStepDto> getRule() { return rule; }
    public void setRule(List<RuleStepDto> rule) { this.rule = rule; }
}
