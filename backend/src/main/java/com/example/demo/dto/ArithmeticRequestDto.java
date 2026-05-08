package com.example.demo.dto;

import com.example.demo.model.ArithmeticOperation;
import com.example.demo.model.Unit;

/**
 * DTO for a weighted arithmetic request (two quantities + operation + target unit).
 */
public class ArithmeticRequestDto {

    private QuantityDto thisQuantity;
    private QuantityDto thatQuantity;
    private ArithmeticOperation operation;
    private Unit resultUnit;

    public ArithmeticRequestDto() {}

	public QuantityDto getThisQuantity() {
		return thisQuantity;
	}

	public void setThisQuantity(QuantityDto thisQuantity) {
		this.thisQuantity = thisQuantity;
	}

	public QuantityDto getThatQuantity() {
		return thatQuantity;
	}

	public void setThatQuantity(QuantityDto thatQuantity) {
		this.thatQuantity = thatQuantity;
	}

	public ArithmeticOperation getOperation() {
		return operation;
	}

	public void setOperation(ArithmeticOperation operation) {
		this.operation = operation;
	}

	public Unit getResultUnit() {
		return resultUnit;
	}

	public void setResultUnit(Unit resultUnit) {
		this.resultUnit = resultUnit;
	}
}