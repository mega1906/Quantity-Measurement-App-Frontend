package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuantityMeasurementRepository;
import com.example.demo.model.*;

@RestController
@RequestMapping("/history")
@CrossOrigin(origins = "http://localhost:3001")
/**
 * Controller that exposes history retrieval and deletion operations.
 */
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);

    private final QuantityMeasurementRepository repository;

    public HistoryController(QuantityMeasurementRepository repository) {
    	this.repository = repository;
    	
    }
    @GetMapping
    public List<QuantityMeasurementEntity> getAll() {
        logger.info("GET /history called");
        return repository.findAll();
    }

    @GetMapping("/operation/{operation}")
    public List<QuantityMeasurementEntity> byOperation(
            @PathVariable OperationType operation) {
        logger.info("GET /history/operation/{} called", operation);
        return repository.findByOperation(operation.name());
    }

    @GetMapping("/type/{measurementType}")
    public List<QuantityMeasurementEntity> byType(
            @PathVariable String measurementType) {
        logger.info("GET /history/type/{} called", measurementType);
        return repository.findByThisMeasurementType(measurementType);
    }

    @DeleteMapping
    public void deleteAll() {
        logger.info("DELETE /history called");
        repository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        logger.info("DELETE /history/{} called", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("History item not found for id: " + id);
        }

        repository.deleteById(id);
    }
}
