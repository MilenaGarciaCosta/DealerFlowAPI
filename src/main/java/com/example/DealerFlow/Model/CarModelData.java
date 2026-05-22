package com.example.DealerFlow.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dealer_code_ml")
public class CarModelData {
    @Id
    private Integer id;

    @Column(name = "ModelName")
    private Integer modelId;

    @Column(name = "ModelYear")
    private Integer modelYear;

    @Column(name = "DaysLastVisit")
    private Integer daysLastVisit;

    @Column(name = "KMLastVisit")
    private Integer kmLastVisit;

    @Column(name = "IsAgendaSchedule")
    private Integer isAgendaSchedule;

    public CarModelData() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Integer getDaysLastVisit() {
        return daysLastVisit;
    }

    public void setDaysLastVisit(Integer daysLastVisit) {
        this.daysLastVisit = daysLastVisit;
    }

    public Integer getKMLastVisit() {
        return kmLastVisit;
    }

    public void setKMLastVisit(Integer kmLastVisit) {
        this.kmLastVisit = kmLastVisit;
    }

    public Integer getIsAgendaSchedule() {
        return isAgendaSchedule;
    }

    public void setIsAgendaSchedule(Integer isAgendaSchedule) {
        this.isAgendaSchedule = isAgendaSchedule;
    }
}