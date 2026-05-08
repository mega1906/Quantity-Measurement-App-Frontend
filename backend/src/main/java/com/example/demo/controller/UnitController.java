package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.MeasurementType;
import com.example.demo.model.Unit;

@RestController
@RequestMapping("/units")
@CrossOrigin(origins = "http://localhost:3001")
/**
 * Controller for unit metadata and unit listing operations.
 */
public class UnitController {

    private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

    @GetMapping("/type/{measurementType}")
    public List<Unit> getUnitsByType(
            @PathVariable MeasurementType measurementType) {
        logger.info("GET /units/type/{} called", measurementType);
        return Arrays.stream(Unit.values())
                .filter(u -> u.getType() == measurementType)
                .toList();
    }

    @PostMapping
    public String createUnit() {
        logger.info("POST /units called (create not allowed) ");
        return "Units are enum-based and cannot be created dynamically";
    }
}
