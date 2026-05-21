package com.example.DealerFlow.Dto;

public class ServiceAnalytics {

    private int serviceCode;
    private String serviceDescription;
    private long serviceCount;
    private double averageDays;
    private double globalAverageDays;

    public ServiceAnalytics() {
    }

    public ServiceAnalytics(int serviceCode, String serviceDescription, long serviceCount,
                            double averageDays, double globalAverageDays) {
        this.serviceCode = serviceCode;
        this.serviceDescription = serviceDescription;
        this.serviceCount = serviceCount;
        this.averageDays = averageDays;
        this.globalAverageDays = globalAverageDays;
    }

    public int getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public long getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(long serviceCount) {
        this.serviceCount = serviceCount;
    }

    public double getAverageDays() {
        return averageDays;
    }

    public void setAverageDays(double averageDays) {
        this.averageDays = averageDays;
    }

    public double getGlobalAverageDays() {
        return globalAverageDays;
    }

    public void setGlobalAverageDays(double globalAverageDays) {
        this.globalAverageDays = globalAverageDays;
    }
}
