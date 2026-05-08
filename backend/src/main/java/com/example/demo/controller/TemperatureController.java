package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.QuantityDto;
import com.example.demo.model.*;
import com.example.demo.service.IQuantityMeasurementService;

@RestController
@RequestMapping("/temperature")
@CrossOrigin(origins = "http://localhost:3001")
/**
 * Controller for temperature-specific conversion endpoints.
 */
public class TemperatureController {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureController.class);

    private final IQuantityMeasurementService service;
    
    public TemperatureController(IQuantityMeasurementService service) {
    	this.service = service;
    }

    @PostMapping("/convert")
    public double convertTemperature(
            @RequestBody QuantityDto dto,
            @RequestParam Unit targetUnit) {
        logger.info("POST /temperature/convert called with dto={} targetUnit={}", dto, targetUnit);
        return service.convert(dto, targetUnit);
    }

    @GetMapping("/scales")
    public List<Unit> scales() {
        logger.info("GET /temperature/scales called");
        return List.of(Unit.CELSIUS, Unit.FAHRENHEIT, Unit.KELVIN);
    }

//    @GetMapping("/absolute-zero")
//    public double absoluteZeroCelsius() {
//        return -273.15;
//    }
}
