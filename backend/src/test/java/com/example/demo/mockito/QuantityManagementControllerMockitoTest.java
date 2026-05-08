package com.example.demo.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.controller.QuantityManagementController;
import com.example.demo.dto.ArithmeticRequestDto;
import com.example.demo.dto.QuantityDto;
import com.example.demo.dto.QuantityInputDto;
import com.example.demo.model.Unit;
import com.example.demo.model.ComparisonResult;
import com.example.demo.service.IQuantityMeasurementService;

@ExtendWith(MockitoExtension.class)
class QuantityManagementControllerMockitoTest {

    @Mock
    private IQuantityMeasurementService service;

    @InjectMocks
    private QuantityManagementController controller;

    @Captor
    private ArgumentCaptor<QuantityDto> quantityDtoCaptor;

    @Test
    void convertGet_callsService() {
        when(service.convert(any(QuantityDto.class), eq(Unit.CENTIMETER))).thenReturn(500.0);

        double result = controller.convertGet(5.0, Unit.METER, Unit.CENTIMETER);
        System.out.println("convertGet result=" + result);

        assertEquals(500.0, result);
        verify(service, times(1)).convert(quantityDtoCaptor.capture(), eq(Unit.CENTIMETER));

        QuantityDto captured = quantityDtoCaptor.getValue();
        assertNotNull(captured);
        System.out.println("captured dto: value=" + captured.getValue() + " unit=" + captured.getUnit());
        assertEquals(5.0, captured.getValue());
        assertEquals(Unit.METER, captured.getUnit());
    }

    @Test
    void convertPost_callsService() {
        when(service.convert(any(QuantityDto.class), eq(Unit.CENTIMETER))).thenReturn(200.0);

        double result = controller.convert(new QuantityDto(2.0, Unit.METER), Unit.CENTIMETER);

        assertEquals(200.0, result);
        verify(service).convert(any(QuantityDto.class), eq(Unit.CENTIMETER));
    }

    @Test
    void arithmetic_callsService() {
        ArithmeticRequestDto request = new ArithmeticRequestDto();
        request.setThisQuantity(new QuantityDto(2.0, Unit.METER));
        request.setThatQuantity(new QuantityDto(3.0, Unit.METER));
        request.setOperation(null);
        request.setResultUnit(null);

        when(service.arithmetic(request)).thenReturn(6.0);

        double result = controller.arithmetic(request);

        assertEquals(6.0, result);
        verify(service).arithmetic(request);
    }

    @Test
    void compare_callsService() {
        QuantityInputDto input = new QuantityInputDto(new QuantityDto(3.0, Unit.METER), new QuantityDto(1.0, Unit.METER));
        when(service.compare(input)).thenReturn("THIS quantity is GREATER than THAT quantity");

        String result = controller.compare(input);

        assertEquals("THIS quantity is GREATER than THAT quantity", result);
        verify(service).compare(input);
    }
}