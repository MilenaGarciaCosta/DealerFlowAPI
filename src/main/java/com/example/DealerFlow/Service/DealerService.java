package com.example.DealerFlow.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DealerService {

    private final JdbcTemplate jdbcTemplate;

    public DealerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isDealerValid(String dealerCode) {
        if (dealerCode == null || dealerCode.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM dealer_code_ml WHERE DealerCode = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dealerCode);

        return count != null && count > 0;
    }
}