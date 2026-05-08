package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.QuantityDto;
import com.example.demo.model.Unit;
import com.example.demo.repository.QuantityMeasurementRepository;

@SpringBootTest
class QuantityMeasurementServiceImplTest {

    @Autowired
    private IQuantityMeasurementService service;

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void clearDb() {
        repository.deleteAll();
    }

    @Test
    void convert_should_save_history() {
        QuantityDto dto = new QuantityDto(1.0, Unit.METER);

        double result = service.convert(dto, Unit.CENTIMETER);

        assertEquals(100.0, result);
        assertEquals(1, repository.count());
    }

    @Test
    void null_unit_should_throw_exception() {
        QuantityDto dto = new QuantityDto();
        dto.setValue(1.0);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.convert(dto, Unit.METER)
        );
    }
}
