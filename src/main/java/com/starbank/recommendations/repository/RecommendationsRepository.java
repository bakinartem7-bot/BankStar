package com.starbank.recommendations.repository;

import com.starbank.recommendations.model.UserAggregates;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserAggregates findAggregatesByUserId(UUID userId) {
        String sql = """
            SELECT
                MAX(CASE WHEN p.type = 'DEBIT' THEN 1 ELSE 0 END) AS has_debit,
                MAX(CASE WHEN p.type = 'INVEST' THEN 1 ELSE 0 END) AS has_invest,
                MAX(CASE WHEN p.type = 'CREDIT' THEN 1 ELSE 0 END) AS has_credit,
                COALESCE(SUM(CASE WHEN p.type = 'DEBIT' AND t.transaction_type = 'DEPOSIT' THEN t.amount ELSE 0 END), 0) AS sum_debit_deposits,
                COALESCE(SUM(CASE WHEN p.type = 'DEBIT' AND t.transaction_type = 'WITHDRAWAL' THEN t.amount ELSE 0 END), 0) AS sum_debit_withdrawals,
                COALESCE(SUM(CASE WHEN p.type = 'SAVING' AND t.transaction_type = 'DEPOSIT' THEN t.amount ELSE 0 END), 0) AS sum_saving_deposits
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ?
            """;

        try {
            UserAggregates agg = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(UserAggregates.class),
                    userId.toString()
            );
            agg.setUserId(userId);
            return agg;
        } catch (EmptyResultDataAccessException e) {
            UserAggregates empty = new UserAggregates();
            empty.setUserId(userId);
            empty.setHasDebit(false);
            empty.setHasInvest(false);
            empty.setHasCredit(false);
            empty.setSumDebitDeposits(0L);
            empty.setSumDebitWithdrawals(0L);
            empty.setSumSavingDeposits(0L);
            return empty;
        }
    }
}
