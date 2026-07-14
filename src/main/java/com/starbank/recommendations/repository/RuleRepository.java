package com.starbank.recommendations.repository;

import com.starbank.recommendations.model.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    List<RuleEntity> findByProductId(String productId);
    List<RuleEntity> findByProductName(String productName);
}
