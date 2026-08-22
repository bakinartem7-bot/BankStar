package com.starbank.recommendations.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rule_query_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryStep implements ConditionStep {  // <-- ключевое изменение: реализуем интерфейс

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false, length = 255)
    private String queryType;

    @Column(columnDefinition = "TEXT")
    private String queryValue;

    @Column(length = 50)
    private String operator;
}
