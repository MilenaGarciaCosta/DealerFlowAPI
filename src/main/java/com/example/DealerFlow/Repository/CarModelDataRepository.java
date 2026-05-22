package com.example.DealerFlow.Repository;

import com.example.DealerFlow.Model.CarModelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface CarModelDataRepository extends JpaRepository<CarModelData, Integer> {

    long countByModelIdAndModelYear(Integer modelId, Integer modelYear);

    @Query(value = "SELECT DaysLastVisit FROM dealer_code_ml " +
            "WHERE ModelName = :modelId AND ModelYear = :modelYear " +
            "AND DaysLastVisit IS NOT NULL AND DaysLastVisit != 0 " +
            "GROUP BY DaysLastVisit " +
            "ORDER BY COUNT(DaysLastVisit) DESC LIMIT 1",
            nativeQuery = true)
    Integer findModeDaysLastVisit(@Param("modelId") Integer modelId, @Param("modelYear") Integer modelYear);


    @Query(value = "SELECT KMLastVisit FROM dealer_code_ml " +
            "WHERE ModelName = :modelId AND ModelYear = :modelYear " +
            "AND KMLastVisit IS NOT NULL AND KMLastVisit != 0 " +
            "GROUP BY KMLastVisit " +
            "ORDER BY COUNT(KMLastVisit) DESC LIMIT 1",
            nativeQuery = true)
    Integer findModeKMLastVisit(@Param("modelId") Integer modelId, @Param("modelYear") Integer modelYear);

    long countByModelIdAndModelYearAndIsAgendaSchedule(Integer modelId, Integer modelYear, Integer isAgendaSchedule);

    @Query(value = "SELECT modelnametable.ModelName FROM dealer_flow.modelnametable", nativeQuery = true)
    List<String> finAllModelNameValues();
}