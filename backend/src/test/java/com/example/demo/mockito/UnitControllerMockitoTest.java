package com.example.demo.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.demo.controller.UnitController;
import com.example.demo.model.MeasurementType;
import com.example.demo.model.Unit;

class UnitControllerMockitoTest {

    private final UnitController controller = new UnitController();

    @Test
    void getUnitsByType_returnsExpectedTypeList() {
        List<Unit> lengthUnits = controller.getUnitsByType(MeasurementType.LENGTH);
        assertEquals(true, lengthUnits.stream().allMatch(u -> u.getType() == MeasurementType.LENGTH));
    }

    @Test
    void createUnit_returnsNotSupportedMessage() {
        assertEquals("Units are enum-based and cannot be created dynamically", controller.createUnit());
    }
}