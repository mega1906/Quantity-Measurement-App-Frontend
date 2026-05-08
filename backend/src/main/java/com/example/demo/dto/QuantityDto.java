package com.example.demo.dto;

import com.example.demo.model.Unit;

/**
 * Data transfer object representing a quantity and its unit.
 */
public class QuantityDto {
	private Double value;
	private Unit unit;

    public QuantityDto() {
    }

	public QuantityDto(Double value, Unit unit) {
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