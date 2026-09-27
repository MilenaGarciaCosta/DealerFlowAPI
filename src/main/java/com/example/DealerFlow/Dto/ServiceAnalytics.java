package com.example.DealerFlow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AnaliseServico", description = "Métricas de tempo de execução de um serviço.")
public class ServiceAnalytics {

    @Schema(description = "Código do serviço.", example = "120")
    private int serviceCode;

    @Schema(description = "Descrição do serviço.", example = "Revisão preventiva")
    private String serviceDescription;

    @Schema(description = "Média de horas do serviço na concessionária.", example = "3")
    private int averageHours;

    @Schema(description = "Média global de horas para o serviço.", example = "4")
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
