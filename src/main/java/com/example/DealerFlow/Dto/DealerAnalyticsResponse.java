package com.example.DealerFlow.Dto;

import java.util.List;

public class DealerAnalyticsResponse {

    private List<DealerAnalytics> dealers;

    public DealerAnalyticsResponse() {
    }

    public DealerAnalyticsResponse(List<DealerAnalytics> dealers) {
        this.dealers = dealers;
    }

    public List<DealerAnalytics> getDealers() {
        return dealers;
    }

    public void setDealers(List<DealerAnalytics> dealers) {
        this.dealers = dealers;
    }
}
