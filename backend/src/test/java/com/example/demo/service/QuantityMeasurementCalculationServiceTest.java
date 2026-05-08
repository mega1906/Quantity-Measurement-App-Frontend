package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.QuantityDto;
import com.example.demo.model.ArithmeticOperation;
import com.example.demo.model.ComparisonResult;
import com.example.demo.model.Unit;

@SpringBootTest
class QuantityMeasurementCalculationServiceTest {

    @Autowired
    private QuantityMeasurementCalculationService service;

    @Test
    void compare_equal_length() {
        QuantityDto a = new QuantityDto(1.0, Unit.METER);
        QuantityDto b = new QuantityDto(100.0, Unit.CENTIMETER);

        ComparisonResult result = service.compare(a, b);

        assertEquals(ComparisonResult.EQUAL, result);
    }

    @Test
    void compare_greater_length() {
        QuantityDto a = new QuantityDto(2.0, Unit.METER);
        QuantityDto b = new QuantityDto(100.0, Unit.CENTIMETER);

        assertEquals(
            ComparisonResult.GREATER,
            service.compare(a, b)
        );
    }

    @Test
    void incompatible_units_should_throw_exception() {
        QuantityDto a = new QuantityDto(1.0, Unit.METER);
        QuantityDto b = new QuantityDto(1.0, Unit.GRAM);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.compare(a, b)
        );
    }
    
    @Test
    void add_with_conversion() {
        QuantityDto a = new QuantityDto(1.0, Unit.METER);
        QuantityDto b = new QuantityDto(50.0, Unit.CENTIMETER);

        double result = service.calculate(
                a, b, ArithmeticOperation.ADD, Unit.METER);

        assertEquals(1.5, result);
    }

    @Test
    void divide_by_zero_should_fail() {
        QuantityDto a = new QuantityDto(10.0, Unit.METER);
        QuantityDto b = new QuantityDto(0.0, Unit.METER);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.calculate(
                a, b, ArithmeticOperation.DIVIDE, Unit.METER)
        );
    }

}
