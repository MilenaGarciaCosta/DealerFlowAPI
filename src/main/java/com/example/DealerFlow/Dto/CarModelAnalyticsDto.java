package com.example.DealerFlow.Dto;

import java.util.List;

public class CarModelAnalyticsDto {
    private Integer modelId;
    private String modelName;
    private Integer year;
    private long count;
    private Integer modeDaysLastVisit;
    private Integer modeKMLastVisit;
    private double scheduledPercentage;
    private double notScheduledPercentage;

    // CORREÇÃO: Alterado de ServiceAnalytics para ModelServiceAnalytics
    private List<ModelServiceAnalytics> topServices;

    public CarModelAnalyticsDto() {
    }

    // CORREÇÃO: O construtor agora recebe List<ModelServiceAnalytics>
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

    // CORREÇÃO: Getters e Setters atualizados para ModelServiceAnalytics
    public List<ModelServiceAnalytics> getTopServices() { return topServices; }
    public void setTopServices(List<ModelServiceAnalytics> topServices) { this.topServices = topServices; }
}