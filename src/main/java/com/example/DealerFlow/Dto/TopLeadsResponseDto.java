package com.example.DealerFlow.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TopLeadsResponseDto {

    private String status;

    @JsonProperty("top_leads")
    private List<ConsultSummaryDto> topLeads;

    public TopLeadsResponseDto() {
    }

    public TopLeadsResponseDto(String status, List<ConsultSummaryDto> topLeads) {
        this.status = status;
        this.topLeads = topLeads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("top_leads")
    public List<ConsultSummaryDto> getTopLeads() {
        return topLeads;
    }

    public void setTopLeads(List<ConsultSummaryDto> topLeads) {
        this.topLeads = topLeads;
    }
}
