package com.example.DealerFlow.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "RespostaPrincipaisLeads", description = "Lista de leads retornados pelo serviço de consulta.")
public class TopLeadsResponseDto {

    @Schema(description = "Status informado pelo serviço de consulta.", example = "success")
    private String status;

    @Schema(description = "Leads mais relevantes conforme os critérios do serviço.")
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
