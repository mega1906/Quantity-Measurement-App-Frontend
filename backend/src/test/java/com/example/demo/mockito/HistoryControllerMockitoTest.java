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

import com.example.demo.controller.HistoryController;
import com.example.demo.model.OperationType;
import com.example.demo.model.QuantityMeasurementEntity;
import com.example.demo.repository.QuantityMeasurementRepository;

@ExtendWith(MockitoExtension.class)
class HistoryControllerMockitoTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private HistoryController controller;

    @Test
    void getAll_returnsAllRecords() {
        List<QuantityMeasurementEntity> all = List.of(new QuantityMeasurementEntity());
        when(repository.findAll()).thenReturn(all);

        assertEquals(all, controller.getAll());
        verify(repository).findAll();
    }

    @Test
    void byOperation_returnsRecordsForOperation() {
        List<QuantityMeasurementEntity> all = List.of(new QuantityMeasurementEntity());
        when(repository.findByOperation(OperationType.CONVERT.name())).thenReturn(all);

        assertEquals(all, controller.byOperation(OperationType.CONVERT));
        verify(repository).findByOperation(OperationType.CONVERT.name());
    }

    @Test
    void byType_returnsRecordsForMeasurementType() {
        List<QuantityMeasurementEntity> all = List.of(new QuantityMeasurementEntity());
        when(repository.findByThisMeasurementType("LENGTH")).thenReturn(all);

        assertEquals(all, controller.byType("LENGTH"));
        verify(repository).findByThisMeasurementType("LENGTH");
    }

    @Test
    void deleteAll_callsDeleteAll() {
        controller.deleteAll();
        verify(repository).deleteAll();
    }
}