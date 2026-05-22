package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.CarModelAnalyticsDto;
import com.example.DealerFlow.Dto.ModelServiceAnalytics;
import com.example.DealerFlow.Repository.CarModelDataRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarModelDataService {

    private final CarModelDataRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public CarModelDataService(CarModelDataRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public CarModelAnalyticsDto buildAnalyticsForModel(Integer modelId, Integer modelYear) {
        long count = getQuantityByModelAndYear(modelId, modelYear);

        if (count == 0) {
            return null;
        }

        Integer modeDays = getModeDaysLastVisit(modelId, modelYear);
        Integer modeKM = getModeKMLastVisit(modelId, modelYear);
        String modelName = getModelNameById(modelId);
        long scheduledCount = getScheduledCount(modelId, modelYear);

        double scheduledPercentage = 0.0;
        double notScheduledPercentage = 0.0;

        if (count > 0) {
            scheduledPercentage = Math.round(((double) scheduledCount / count * 100) * 100.0) / 100.0;
            notScheduledPercentage = Math.round(((double) (count - scheduledCount) / count * 100) * 100.0) / 100.0;
        }

        List<ModelServiceAnalytics> topServices = getTopServicesForModel(modelId, modelYear);

        return new CarModelAnalyticsDto(
                modelId,
                modelName,
                modelYear,
                count,
                modeDays,
                modeKM,
                scheduledPercentage,
                notScheduledPercentage,
                topServices
        );
    }

    public List<ModelServiceAnalytics> getTopServicesForModel(Integer modelId, Integer modelYear) {
        String sql = """
                SELECT d.ServiceCode, s.description,
                       COUNT(*) AS serviceCount
                FROM dealer_code_ml d
                JOIN servicecode s ON d.ServiceCode = s.code
                WHERE d.ModelName = ? AND d.ModelYear = ?
                GROUP BY d.ServiceCode, s.description
                ORDER BY serviceCount DESC
                LIMIT 3
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ModelServiceAnalytics(
                rs.getInt("ServiceCode"),
                rs.getString("description")
        ), modelId, modelYear);
    }

    public Map<Integer, Integer> getGlobalAverages(List<Integer> serviceCodes) {
        if (serviceCodes == null || serviceCodes.isEmpty()) {
            return Collections.emptyMap();
        }

        String placeholders = serviceCodes.stream()
                .map(c -> "?")
                .collect(Collectors.joining(", "));

        String sql = """
                SELECT ServiceCode,
                       AVG(DATEDIFF(STR_TO_DATE(ServiceClosedDate, '%%m/%%d/%%Y'),
                                     STR_TO_DATE(ServiceOpenDate, '%%m/%%d/%%Y'))) * 24 AS globalAverageHours
                FROM dealer_code_ml
                WHERE ServiceCode IN (%s)
                  AND ServiceClosedDate IS NOT NULL AND ServiceClosedDate <> ''
                  AND ServiceOpenDate IS NOT NULL AND ServiceOpenDate <> ''
                GROUP BY ServiceCode
                """.formatted(placeholders);

        Map<Integer, Integer> result = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            result.put(rs.getInt("ServiceCode"), rs.getInt("globalAverageHours"));
        }, serviceCodes.toArray());

        return result;
    }

    public long getQuantityByModelAndYear(Integer modelId, Integer modelYear) {
        return repository.countByModelIdAndModelYear(modelId, modelYear);
    }

    public Integer getModeDaysLastVisit(Integer modelId, Integer modelYear) {
        return repository.findModeDaysLastVisit(modelId, modelYear);
    }

    public Integer getModeKMLastVisit(Integer modelId, Integer modelYear) {
        return repository.findModeKMLastVisit(modelId, modelYear);
    }

    public String getModelNameById(Integer modelId) {
        String sql = "SELECT ModelName FROM modelnametable WHERE ID_ModelName = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, modelId);
        } catch (EmptyResultDataAccessException e) {
            return "Modelo Desconhecido";
        } catch (Exception e) {
            System.err.println("=== ERRO REAL DO JDBC ===");
            System.err.println(e.getMessage());
            return "Erro ao buscar nome";
        }
    }

    public long getScheduledCount(Integer modelId, Integer modelYear) {
        return repository.countByModelIdAndModelYearAndIsAgendaSchedule(modelId, modelYear, 1);
    }

    public List<String> getAllCarModelNames(){
        return repository.finAllModelNameValues();
    }
}