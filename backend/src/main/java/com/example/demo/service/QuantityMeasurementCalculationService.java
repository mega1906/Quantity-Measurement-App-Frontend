package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.QuantityDto;
import com.example.demo.model.ArithmeticOperation;
import com.example.demo.model.ComparisonResult;
import com.example.demo.model.Unit;

@Service
public class QuantityMeasurementCalculationService {

    private final QuantityMeasurementConversionService conversionService;

    public QuantityMeasurementCalculationService(QuantityMeasurementConversionService conversionService) {
        this.conversionService = conversionService;
    }
//
//    public double add(QuantityDto a, QuantityDto b, Unit targetUnit) {
//        double bConverted =conversionService.convert(b.getValue(), b.getUnit(), a.getUnit());
//
//        double result = a.getValue() + bConverted;
//
//        return conversionService.convert(result, a.getUnit(), targetUnit);
//    }

    public double calculate(
            QuantityDto a,
            QuantityDto b,
            ArithmeticOperation operation,
            Unit resultUnit) {

        // Convert second operand into the unit of first operand before arithmetic.
        double bConverted =
            conversionService.convert(b.getValue(), b.getUnit(), a.getUnit());

        double intermediate;

        switch (operation) {
            case ADD -> intermediate = a.getValue() + bConverted;
            case SUBTRACT -> intermediate = a.getValue() - bConverted;
            case MULTIPLY -> intermediate = a.getValue() * bConverted;
            case DIVIDE -> {
                if (bConverted == 0) {
                    throw new IllegalArgumentException("Division by zero");
                }
                intermediate = a.getValue() / bConverted;
            }
            default -> throw new IllegalStateException("Invalid operation");
        }

        // Convert the intermediate result to the requested result unit.
        return conversionService.convert(
                intermediate, a.getUnit(), resultUnit);
    }

    /**
     * Compare two quantities using normalized units and map to enum.
     *
     * @param a first quantity
     * @param b second quantity
     * @return ComparisonResult enum value GREATER/LESSER/EQUAL
     */
    public ComparisonResult compare(QuantityDto a, QuantityDto b) {
        double bConverted = conversionService.convert(b.getValue(), b.getUnit(), a.getUnit());

        int result = Double.compare(a.getValue(), bConverted);

        if (result > 0) return ComparisonResult.GREATER;
        if (result < 0) return ComparisonResult.LESSER;
        return ComparisonResult.EQUAL;
    }
}