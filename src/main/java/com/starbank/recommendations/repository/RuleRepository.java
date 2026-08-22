package com.starbank.recommendations.repository;

import com.starbank.recommendations.model.Rule;
import com.starbank.recommendations.dto.RuleStatsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findAllByIsActiveTrue();
    @Query("SELECT new com.starbank.recommendations.dto.RuleStatsDto(r.name, r.description, r.applyCount) " +
            "FROM Rule r WHERE r.active = true ORDER BY r.applyCount DESC")
    List<RuleStatsDto> findActiveRulesWithStats();
}
