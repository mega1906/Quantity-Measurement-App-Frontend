package com.example.demo.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.controller.TemperatureController;
import com.example.demo.dto.QuantityDto;
import com.example.demo.model.Unit;
import com.example.demo.service.QuantityMeasurementConversionService;

@ExtendWith(MockitoExtension.class)
class TemperatureControllerMockitoTest {

    @Mock
    private QuantityMeasurementConversionService conversionService;

    @InjectMocks
    private TemperatureController controller;

    @Test
    void convertTemperature_delegatesToConversionService() {
        QuantityDto dto = new QuantityDto(0.0, Unit.CELSIUS);
        when(conversionService.convert(0.0, Unit.CELSIUS, Unit.FAHRENHEIT)).thenReturn(32.0);

        double result = controller.convertTemperature(dto, Unit.FAHRENHEIT);

        assertEquals(32.0, result);
        verify(conversionService).convert(0.0, Unit.CELSIUS, Unit.FAHRENHEIT);
    }

    @Test
    void scales_returnsTemperatureUnits() {
        assertEquals(List.of(Unit.CELSIUS, Unit.FAHRENHEIT), controller.scales());
    }
}