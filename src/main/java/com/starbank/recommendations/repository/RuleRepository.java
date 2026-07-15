package com.starbank.recommendations.repository;

import com.starbank.recommendations.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Rule r WHERE r.productId = :productId")
    void deleteByProductId(String productId);
}
