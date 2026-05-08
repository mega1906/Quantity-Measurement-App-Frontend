package com.example.demo.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.demo.controller.ConversionController;
import com.example.demo.model.Unit;

class ConversionControllerMockitoTest {

    private final ConversionController controller = new ConversionController();

    @Test
    void getAllConversions_returnsManyPairs() {
        List<String> result = controller.getAllConversions();

        assertEquals(true, result.size() > 0);
    }

    @Test
    void validateConversion_compatible_returnsTrue() {
        assertEquals(true, controller.validateConversion(Unit.METER, Unit.CENTIMETER));
    }

    @Test
    void validateConversion_incompatible_returnsFalse() {
        assertEquals(false, controller.validateConversion(Unit.METER, Unit.CELSIUS));
    }
}