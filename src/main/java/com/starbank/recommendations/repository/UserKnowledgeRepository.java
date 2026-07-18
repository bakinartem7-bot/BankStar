package com.starbank.recommendations.repository;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserKnowledgeRepository {

    private static final String SQL_COUNT_USER_PRODUCT =
            "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND product_type = ?";

    private static final String SQL_SUM_AMOUNT =
            "SELECT COALESCE(SUM(amount), 0) FROM transactions " +
                    "WHERE user_id = ? AND product_type = ? AND transaction_type = ?";

    private static final String SQL_SUM_DEPOSIT =
            "SELECT COALESCE(SUM(amount), 0) FROM transactions " +
                    "WHERE user_id = ? AND product_type = ? AND transaction_type = 'DEPOSIT'";

    private static final String SQL_SUM_WITHDRAW =
            "SELECT COALESCE(SUM(amount), 0) FROM transactions " +
                    "WHERE user_id = ? AND product_type = ? AND transaction_type = 'WITHDRAW'";

    private static final int ACTIVE_USER_THRESHOLD = 5;

    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> countCache;
    private final Cache<String, BigDecimal> sumCache;
    private final Cache<String, Map<String, BigDecimal>> depositWithdrawCache;

    public UserKnowledgeRepository(
            JdbcTemplate jdbcTemplate,
            Cache<String, Boolean> countCache,
            Cache<String, BigDecimal> sumCache,
            Cache<String, Map<String, BigDecimal>> depositWithdrawCache) {
        this.jdbcTemplate = jdbcTemplate;
        this.countCache = countCache;
        this.sumCache = sumCache;
        this.depositWithdrawCache = depositWithdrawCache;
    }

    public boolean isUserOfProduct(long userId, String productType) {
        String key = userId + ":USER_OF:" + productType;
        return countCache.get(key, k -> {
            long count = jdbcTemplate.queryForObject(SQL_COUNT_USER_PRODUCT, Long.class, userId, productType);
            return count > 0;
        });
    }

    public boolean isActiveUserOfProduct(long userId, String productType) {
        String key = userId + ":ACTIVE_USER_OF:" + productType;
        return countCache.get(key, k -> {
            long count = jdbcTemplate.queryForObject(SQL_COUNT_USER_PRODUCT, Long.class, userId, productType);
            return count >= ACTIVE_USER_THRESHOLD;
        });
    }

    public boolean checkTransactionSumCompare(long userId, String productType, String transactionType, String operator, int constant) {
        String key = userId + ":SUM:" + productType + ":" + transactionType + ":" + constant + ":" + operator;
        BigDecimal sum = sumCache.get(key, k ->
                jdbcTemplate.queryForObject(SQL_SUM_AMOUNT, BigDecimal.class, userId, productType, transactionType)
        );
        return compare(sum, operator, constant);
    }

    public boolean checkDepositWithdrawCompare(long userId, String productType, String operator) {
        String key = userId + ":DEPOSIT_WITHDRAW:" + productType + ":" + operator;
        Map<String, BigDecimal> sums = depositWithdrawCache.get(key, k -> {
            Map<String, BigDecimal> m = new HashMap<>();
            m.put("deposit", jdbcTemplate.queryForObject(SQL_SUM_DEPOSIT, BigDecimal.class, userId, productType));
            m.put("withdraw", jdbcTemplate.queryForObject(SQL_SUM_WITHDRAW, BigDecimal.class, userId, productType));
            return m;
        });
        BigDecimal deposit = sums.getOrDefault("deposit", BigDecimal.ZERO);
        BigDecimal withdraw = sums.getOrDefault("withdraw", BigDecimal.ZERO);
        return compareTwo(deposit, withdraw, operator);
    }

    private static boolean compare(BigDecimal sum, String operator, int constant) {
        int cmp = sum.compareTo(BigDecimal.valueOf(constant));
        return switch (operator) {
            case ">"  -> cmp > 0;
            case "<"  -> cmp < 0;
            case "="  -> cmp == 0;
            case ">=" -> cmp >= 0;
            case "<=" -> cmp <= 0;
            default   -> false;
        };
    }

    private static boolean compareTwo(BigDecimal a, BigDecimal b, String operator) {
        int cmp = a.compareTo(b);
        return switch (operator) {
            case ">"  -> cmp > 0;
            case "<"  -> cmp < 0;
            case "="  -> cmp == 0;
            case ">=" -> cmp >= 0;
            case "<=" -> cmp <= 0;
            default   -> false;
        };
    }
}
