package com.example.DealerFlow.Dto;

import java.util.List;

public class DealerAnalytics {

    private String dealerCode;
    private List<ServiceAnalytics> topServices;

    public DealerAnalytics() {
    }

    public DealerAnalytics(String dealerCode, List<ServiceAnalytics> topServices) {
        this.dealerCode = dealerCode;
        this.topServices = topServices;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public List<ServiceAnalytics> getTopServices() {
        return topServices;
    }

    public void setTopServices(List<ServiceAnalytics> topServices) {
        this.topServices = topServices;
    }
}
