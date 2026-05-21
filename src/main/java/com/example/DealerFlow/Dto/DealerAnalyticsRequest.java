package com.example.DealerFlow.Dto;

import java.util.List;

public class DealerAnalyticsRequest {

    private List<String> dealerCodes;

    public DealerAnalyticsRequest() {
    }

    public DealerAnalyticsRequest(List<String> dealerCodes) {
        this.dealerCodes = dealerCodes;
    }

    public List<String> getDealerCodes() {
        return dealerCodes;
    }

    public void setDealerCodes(List<String> dealerCodes) {
        this.dealerCodes = dealerCodes;
    }
}
