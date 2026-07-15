package com.starbank.recommendations.dto;

import com.starbank.recommendations.model.Rule;
import java.time.LocalDateTime;
import java.util.List;

public class RuleRequestDto {
    private String productName;
    private String productId;      // user_id по ТЗ
    private String productText;
    private List<RuleStepDto> rule;

    public Rule toRule() {
        Rule r = new Rule();
        r.setProductId(this.productId);
        r.setProductName(this.productName);
        r.setProductText(this.productText);
        r.setCreatedAt(LocalDateTime.now());
        return r;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public List<RuleStepDto> getRule() { return rule; }
    public void setRule(List<RuleStepDto> rule) { this.rule = rule; }
}
