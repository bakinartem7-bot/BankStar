package com.starbank.recommendations.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rule_query_step")
public class QueryStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private RuleEntity rule;

    @Column(nullable = false)
    private String query;

    @Column
    private String argumentsJson;

    private boolean negate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RuleEntity getRule() { return rule; }
    public void setRule(RuleEntity rule) { this.rule = rule; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getArgumentsJson() { return argumentsJson; }
    public void setArgumentsJson(String argumentsJson) { this.argumentsJson = argumentsJson; }

    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }
}
