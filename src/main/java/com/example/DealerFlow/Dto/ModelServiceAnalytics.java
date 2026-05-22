package com.example.DealerFlow.Dto;

public class ModelServiceAnalytics {
    private Integer serviceCode;
    private String serviceDescription;

    public ModelServiceAnalytics(Integer serviceCode, String serviceDescription) {
        this.serviceCode = serviceCode;
        this.serviceDescription = serviceDescription;
    }

    public Integer getServiceCode() { return serviceCode; }
    public String getServiceDescription() { return serviceDescription; }
}