package com.example.DealerFlow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "AnaliseModeloVeiculo", description = "Indicadores agregados de um modelo de veículo em determinado ano.")
public class CarModelAnalyticsDto {
    @Schema(description = "Identificador do modelo.", example = "12")
    private Integer modelId;
    @Schema(description = "Nome do modelo.", example = "City")
    private String modelName;
    @Schema(description = "Ano analisado.", example = "2024")
    private Integer year;
    @Schema(description = "Quantidade de registros considerados.", example = "184")
    private long count;
    @Schema(description = "Moda de dias desde a última visita.", example = "180")
    private Integer modeDaysLastVisit;
    @Schema(description = "Moda da quilometragem desde a última visita.", example = "10000")
    private Integer modeKMLastVisit;
    @Schema(description = "Percentual de atendimentos agendados.", example = "72.5")
    private double scheduledPercentage;
    @Schema(description = "Percentual de atendimentos não agendados.", example = "27.5")
    private double notScheduledPercentage;

    @Schema(description = "Serviços mais frequentes para o modelo e ano.")
    private List<ModelServiceAnalytics> topServices;

    public CarModelAnalyticsDto() {
    }

    public CarModelAnalyticsDto(Integer modelId, String modelName, Integer year, long count, Integer modeDaysLastVisit, Integer modeKMLastVisit, double scheduledPercentage, double notScheduledPercentage, List<ModelServiceAnalytics> topServices) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.year = year;
        this.count = count;
        this.modeDaysLastVisit = modeDaysLastVisit;
        this.modeKMLastVisit = modeKMLastVisit;
        this.scheduledPercentage = scheduledPercentage;
        this.notScheduledPercentage = notScheduledPercentage;
        this.topServices = topServices;
    }

    public Integer getModelId() { return modelId; }
    public void setModelId(Integer modelId) { this.modelId = modelId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }

    public Integer getModeDaysLastVisit() { return modeDaysLastVisit; }
    public void setModeDaysLastVisit(Integer modeDaysLastVisit) { this.modeDaysLastVisit = modeDaysLastVisit; }

    public Integer getModeKMLastVisit() { return modeKMLastVisit; }
    public void setModeKMLastVisit(Integer modeKMLastVisit) { this.modeKMLastVisit = modeKMLastVisit; }

    public double getScheduledPercentage() { return scheduledPercentage; }
    public void setScheduledPercentage(double scheduledPercentage) { this.scheduledPercentage = scheduledPercentage; }

    public double getNotScheduledPercentage() { return notScheduledPercentage; }
    public void setNotScheduledPercentage(double notScheduledPercentage) { this.notScheduledPercentage = notScheduledPercentage; }

    public List<ModelServiceAnalytics> getTopServices() { return topServices; }
    public void setTopServices(List<ModelServiceAnalytics> topServices) { this.topServices = topServices; }
}