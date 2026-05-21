package com.example.DealerFlow.Dto;

public class ServiceAnalytics {

    private int serviceCode;
    private String serviceDescription;
    private int averageHours;
    private int globalAverageHours;

    public ServiceAnalytics() {
    }

    public ServiceAnalytics(int serviceCode, String serviceDescription,
                            int averageHours, int globalAverageHours) {
        this.serviceCode = serviceCode;
        this.serviceDescription = serviceDescription;
        this.averageHours = averageHours;
        this.globalAverageHours = globalAverageHours;
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

    public int getAverageHours() {
        return averageHours;
    }

    public void setAverageHours(int averageHours) {
        this.averageHours = averageHours;
    }

    public int getGlobalAverageHours() {
        return globalAverageHours;
    }

    public void setGlobalAverageHours(int globalAverageHours) {
        this.globalAverageHours = globalAverageHours;
    }
}
