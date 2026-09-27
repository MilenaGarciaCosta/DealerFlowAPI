package com.example.DealerFlow.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "RespostaConsulta", description = "Resultado de uma consulta individual de propensão.")
public class ConsultResponseDto {

    @Schema(description = "Status informado pelo serviço de consulta.", example = "success")
    private String status;

    @Schema(description = "Identificador do registro.", example = "98765")
    @JsonProperty("ID")
    private Long id;

    @Schema(description = "Hash do VIN associado ao registro.", example = "a1b2c3d4e5")
    @JsonProperty("VIN_Hash")
    private String vinHash;

    @Schema(description = "Pontuação de propensão expressa em percentual.", example = "83.2500", nullable = true)
    @JsonProperty("propensity_score")
    private BigDecimal propensityScore;

    public ConsultResponseDto() {
    }

    public ConsultResponseDto(String status, Long id, String vinHash, BigDecimal propensityScore) {
        this.status = status;
        this.id = id;
        this.vinHash = vinHash;
        this.propensityScore = propensityScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("VIN_Hash")
    public String getVinHash() {
        return vinHash;
    }

    public void setVinHash(String vinHash) {
        this.vinHash = vinHash;
    }

    @JsonProperty("propensity_score")
    public BigDecimal getPropensityScore() {
        return propensityScore;
    }

    public void setPropensityScore(BigDecimal propensityScore) {
        this.propensityScore = propensityScore;
    }
}
