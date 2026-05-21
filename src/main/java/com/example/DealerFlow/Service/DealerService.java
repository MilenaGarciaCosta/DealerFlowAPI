package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.DealerAnalytics;
import com.example.DealerFlow.Dto.ServiceAnalytics;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<String> getAllDealerCodes() {
        String sql = "SELECT DISTINCT DealerCode FROM dealer_code_ml";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<ServiceAnalytics> getTopServicesForDealer(String dealerCode) {
        String sql = """
                SELECT d.ServiceCode, s.description,
                       COUNT(*) AS serviceCount,
                       AVG(DATEDIFF(STR_TO_DATE(d.ServiceClosedDate, '%m/%d/%Y'),
                                     STR_TO_DATE(d.ServiceOpenDate, '%m/%d/%Y'))) AS averageDays
                FROM dealer_code_ml d
                JOIN servicecode s ON d.ServiceCode = s.code
                WHERE d.DealerCode = ?
                  AND d.ServiceClosedDate IS NOT NULL AND d.ServiceClosedDate <> ''
                  AND d.ServiceOpenDate IS NOT NULL AND d.ServiceOpenDate <> ''
                GROUP BY d.ServiceCode, s.description
                ORDER BY serviceCount DESC
                LIMIT 3
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ServiceAnalytics(
                rs.getInt("ServiceCode"),
                rs.getString("description"),
                rs.getLong("serviceCount"),
                rs.getDouble("averageDays"),
                0.0
        ), dealerCode);
    }

    public Map<Integer, Double> getGlobalAverages(List<Integer> serviceCodes) {
        if (serviceCodes == null || serviceCodes.isEmpty()) {
            return Collections.emptyMap();
        }

        String placeholders = serviceCodes.stream()
                .map(c -> "?")
                .collect(Collectors.joining(", "));

        String sql = """
                SELECT ServiceCode,
                       AVG(DATEDIFF(STR_TO_DATE(ServiceClosedDate, '%%m/%%d/%%Y'),
                                     STR_TO_DATE(ServiceOpenDate, '%%m/%%d/%%Y'))) AS globalAverageDays
                FROM dealer_code_ml
                WHERE ServiceCode IN (%s)
                  AND ServiceClosedDate IS NOT NULL AND ServiceClosedDate <> ''
                  AND ServiceOpenDate IS NOT NULL AND ServiceOpenDate <> ''
                GROUP BY ServiceCode
                """.formatted(placeholders);

        Map<Integer, Double> result = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            result.put(rs.getInt("ServiceCode"), rs.getDouble("globalAverageDays"));
        }, serviceCodes.toArray());

        return result;
    }

    public DealerAnalytics buildAnalyticsForDealer(String dealerCode) {
        List<ServiceAnalytics> topServices = getTopServicesForDealer(dealerCode);

        List<Integer> codes = topServices.stream()
                .map(ServiceAnalytics::getServiceCode)
                .toList();

        Map<Integer, Double> globalAverages = getGlobalAverages(codes);

        List<ServiceAnalytics> enriched = topServices.stream()
                .map(s -> new ServiceAnalytics(
                        s.getServiceCode(),
                        s.getServiceDescription(),
                        s.getServiceCount(),
                        s.getAverageDays(),
                        globalAverages.getOrDefault(s.getServiceCode(), 0.0)
                ))
                .toList();

        return new DealerAnalytics(dealerCode, enriched);
    }
}