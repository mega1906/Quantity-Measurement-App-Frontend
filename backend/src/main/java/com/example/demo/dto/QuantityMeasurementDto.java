package com.example.demo.dto;

import com.example.demo.model.Unit;

/**
 * DTO for storing or transferring a measurement value + unit in history operations.
 */
public class QuantityMeasurementDto {

    private Double value;
    private Unit unit;

    public QuantityMeasurementDto() {
    }

    public QuantityMeasurementDto(Double value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}