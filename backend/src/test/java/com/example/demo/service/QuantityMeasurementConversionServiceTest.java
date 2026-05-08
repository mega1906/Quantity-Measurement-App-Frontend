package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.QuantityDto;
import com.example.demo.model.Unit;

@SpringBootTest
class QuantityMeasurementConversionServiceTest {

    @Autowired
    private QuantityMeasurementConversionService conversionService;

    @Test
    void temperature_conversion_celsius_to_fahrenheit() {

        QuantityDto dto = new QuantityDto(0.0, Unit.CELSIUS);

        double result = conversionService.convert(
                dto.getValue(),
                dto.getUnit(),
                Unit.FAHRENHEIT
        );

        assertEquals(32.0, result);
    }
    
    @Test
    void temperature_conversion_fahrenheit_to_celsius() {
        double result = conversionService.convert(
                32.0, Unit.FAHRENHEIT, Unit.CELSIUS);

        assertEquals(0.0, result);
    }

    @Test
    void same_temperature_unit_returns_same_value() {
        double result = conversionService.convert(
                25.0, Unit.CELSIUS, Unit.CELSIUS);

        assertEquals(25.0, result);
    }
}