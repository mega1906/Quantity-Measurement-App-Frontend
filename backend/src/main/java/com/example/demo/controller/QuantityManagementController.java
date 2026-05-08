package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ArithmeticRequestDto;
import com.example.demo.dto.QuantityDto;
import com.example.demo.dto.QuantityInputDto;
import com.example.demo.model.Unit;
import com.example.demo.service.IQuantityMeasurementService;

@RestController
@RequestMapping("/api/v1/quantities")
@CrossOrigin(origins = "http://localhost:3001")
/**
 * Primary REST controller for quantity operations (convert/arithmetic/compare).
 */
public class QuantityManagementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityManagementController.class);

    private final IQuantityMeasurementService service;

    public QuantityManagementController(IQuantityMeasurementService service) {
    	this.service = service;
    }
    @GetMapping("/convert")
    public double convertGet(
            @RequestParam double value,
            @RequestParam Unit unit,
            @RequestParam Unit targetUnit) {
        logger.info("GET /api/v1/quantities/convert called with value={}, unit={}, targetUnit={}", value, unit, targetUnit);
        QuantityDto dto = new QuantityDto();
        dto.setValue(value);
        dto.setUnit(unit);
        return service.convert(dto, targetUnit);
    }

    @PostMapping("/convert")
    public double convert(
            @RequestBody QuantityDto dto,
            @RequestParam Unit targetUnit) {
        logger.info("POST /api/v1/quantities/convert called with dto={} targetUnit={}", dto, targetUnit);
        return service.convert(dto, targetUnit);
    }

    @PostMapping("/arithmetic")
    public double arithmetic(@RequestBody ArithmeticRequestDto request) {
        logger.info("POST /api/v1/quantities/arithmetic called with request={}", request);
        return service.arithmetic(request);
    }

    @PostMapping("/compare")
    public String compare(@RequestBody QuantityInputDto input) {
        logger.info("POST /api/v1/quantities/compare called with input={}", input);
        return service.compare(input);
    }
}
