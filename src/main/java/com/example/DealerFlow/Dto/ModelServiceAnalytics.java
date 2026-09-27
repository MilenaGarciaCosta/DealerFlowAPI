package com.example.DealerFlow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ServicoDoModelo", description = "Serviço associado à análise do modelo de veículo.")
public class ModelServiceAnalytics {
    @Schema(description = "Código do serviço.", example = "120")
    private Integer serviceCode;
    @Schema(description = "Descrição do serviço.", example = "Revisão preventiva")
    private String serviceDescription;

    public ModelServiceAnalytics(Integer serviceCode, String serviceDescription) {
        this.serviceCode = serviceCode;
        this.serviceDescription = serviceDescription;
    }

    public Integer getServiceCode() { return serviceCode; }
    public String getServiceDescription() { return serviceDescription; }
}