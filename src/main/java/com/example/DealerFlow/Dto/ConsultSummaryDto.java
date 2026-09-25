package com.example.DealerFlow.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ConsultSummaryDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("VIN_Hash")
    private String vinHash;

    @JsonProperty("propensity_score")
    private BigDecimal propensityScore;

    public ConsultSummaryDto() {
    }

    public ConsultSummaryDto(Long id, String vinHash, BigDecimal propensityScore) {
        this.id = id;
        this.vinHash = vinHash;
        this.propensityScore = propensityScore;
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
