package com.starbank.recommendations.config;

import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.model.FixedStep;
import com.starbank.recommendations.model.ConditionStep;
import java.util.*;

public class StaticRules {

    public static final List<Rule> STATIC_RULES = List.of(
            createRule("Age > 25", "PROD-A", "Рекомендация для клиентов старше 25 лет"),
            createRule("Income >= 50000", "PROD-B", "Рекомендация для клиентов с доходом от 50 тыс.")
    );

    private static final Map<String, List<FixedStep>> STATIC_STEPS = new HashMap<>();

    static {
        STATIC_STEPS.put("Age > 25", List.of(new FixedStep("AGE", "25", "GT")));
        STATIC_STEPS.put("Income >= 50000", List.of(new FixedStep("INCOME", "50000", "GE")));
    }

    public static List<FixedStep> getStepsForRule(String ruleName) {
        return STATIC_STEPS.getOrDefault(ruleName, Collections.emptyList());
    }

    private static Rule createRule(String name, String productId, String description) {
        Rule rule = new Rule();
        rule.setName(name);
        rule.setProductId(productId);
        rule.setDescription(description);
        rule.setActive(true);
        rule.setApplyCount(0L);
        return rule;
    }
}
