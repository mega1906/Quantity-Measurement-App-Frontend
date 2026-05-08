package com.example.demo.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.ArithmeticRequestDto;
import com.example.demo.dto.QuantityDto;
import com.example.demo.dto.QuantityInputDto;
import com.example.demo.model.ArithmeticOperation;
import com.example.demo.model.ComparisonResult;
import com.example.demo.model.OperationType;
import com.example.demo.model.QuantityMeasurementEntity;
import com.example.demo.model.Unit;
import com.example.demo.repository.QuantityMeasurementRepository;
import com.example.demo.service.QuantityMeasurementCalculationService;
import com.example.demo.service.QuantityMeasurementConversionService;
import com.example.demo.service.QuantityMeasurementServiceImpl;

@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceImplMockitoTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @Mock
    private QuantityMeasurementCalculationService calculationService;

    @Mock
    private QuantityMeasurementConversionService conversionService;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    private QuantityDto quantityDto;

    @BeforeEach
    void setUp() {
        quantityDto = new QuantityDto(1.0, Unit.METER);
    }

    @Test
    void convert_succeeds_and_saves_history() {
        when(conversionService.convert(1.0, Unit.METER, Unit.CENTIMETER)).thenReturn(100.0);

        double result = service.convert(quantityDto, Unit.CENTIMETER);

        assertEquals(100.0, result);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void convert_null_unit_throws_and_saves_error_history() {
        quantityDto.setUnit(null);

        assertThrows(IllegalArgumentException.class, () -> service.convert(quantityDto, Unit.CENTIMETER));
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void compare_returns_result_and_saves_history() {
        QuantityInputDto input = new QuantityInputDto(quantityDto, new QuantityDto(0.5, Unit.METER));

        when(calculationService.compare(quantityDto, input.getThatQuantityDto())).thenReturn(ComparisonResult.GREATER);

        String actual = service.compare(input);

        assertEquals("THIS quantity is GREATER than THAT quantity", actual);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void getHistoryByOperation_usesRepository() {
        List<QuantityMeasurementEntity> history = Collections.singletonList(new QuantityMeasurementEntity());
        when(repository.findByOperation(OperationType.CONVERT.name())).thenReturn(history);

        List<QuantityMeasurementEntity> result = service.getHistoryByOperation(OperationType.CONVERT);

        assertEquals(history, result);
    }

    // @Test
    // void arithmetic_delegates_to_calculation_and_saves_history() {
    //     ArithmeticRequestDto request = new ArithmeticRequestDto();
    //     request.setThisQuantity(quantityDto);
    //     request.setThatQuantity(new QuantityDto(2.0, Unit.METER));
    //     request.setOperation(com.example.demo.model.ArithmeticOperation.MULTIPLY);
    //     request.setResultUnit(Unit.METER);

    //     when(calculationService.calculate(request.getThisQuantity(), request.getThatQuantity(), request.getOperation(), request.getResultUnit()))
    //         .thenReturn(2.0);

    //     double result = service.arithmetic(request);

    //     assertEquals(2.0, result);
    //     verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    // }
     @Test
    void arithmetic_delegates_to_calculation_and_saves_history() {
        // Arrange
        QuantityDto quantityDto = new QuantityDto(1.0, Unit.METER);

        ArithmeticRequestDto request = new ArithmeticRequestDto();
        request.setThisQuantity(quantityDto);
        request.setThatQuantity(new QuantityDto(2.0, Unit.METER));
        request.setOperation(ArithmeticOperation.MULTIPLY);
        request.setResultUnit(Unit.METER);

        when(calculationService.calculate(
                any(QuantityDto.class),
                any(QuantityDto.class),
                any(ArithmeticOperation.class),
                any(Unit.class)
        )).thenReturn(2.0);

        // Act
        double result = service.arithmetic(request);

        // Assert
        assertEquals(2.0, result);

        verify(calculationService, times(1)).calculate(
                any(QuantityDto.class),
                any(QuantityDto.class),
                any(ArithmeticOperation.class),
                any(Unit.class)
        );

        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }
}
