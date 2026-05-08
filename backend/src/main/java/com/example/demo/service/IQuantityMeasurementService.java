package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ArithmeticRequestDto;
import com.example.demo.dto.QuantityDto;
import com.example.demo.dto.QuantityInputDto;
import com.example.demo.model.Unit;
import com.example.demo.model.QuantityMeasurementEntity;
import com.example.demo.model.OperationType;

public interface IQuantityMeasurementService {

    /**
     * Convert a quantity from its current unit to the target unit.
     *
     * @param quantity  quantity value + unit to convert
     * @param targetUnit unit to convert to
     * @return converted value as double
     */
    double convert(QuantityDto quantity, Unit targetUnit);

    /**
     * Compare two quantity inputs and return a comparison result string.
     *
     * @param input comparison payload (value1/unit1 vs value2/unit2)
     * @return "GREATER", "LESSER", or "EQUAL"
     */
    String compare(QuantityInputDto input);

    /**
     * Retrieve persisted operation history filtered by an operation type.
     *
     * @param operation operation type (CONVERT, ADD, SUBTRACT, MULTIPLY, DIVIDE, etc.)
     * @return list of history entities matching the operation
     */
    List<QuantityMeasurementEntity> getHistoryByOperation(OperationType operation);

    /**
     * Perform arithmetic on two quantities with a given operation.
     *
     * @param request arithmetic request containing operands and operation
     * @return result as double
     */
    double arithmetic(ArithmeticRequestDto request);
}
