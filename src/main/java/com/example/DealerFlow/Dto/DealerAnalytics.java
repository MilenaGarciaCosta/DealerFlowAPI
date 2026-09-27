package com.example.DealerFlow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "AnaliseConcessionaria", description = "Indicadores e serviços em destaque para uma concessionária.")
public class DealerAnalytics {

    @Schema(description = "Código da concessionária analisada.", example = "DLR-001")
    private String dealerCode;

    @Schema(description = "Serviços com melhor desempenho para a concessionária.")
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
