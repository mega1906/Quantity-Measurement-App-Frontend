package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Unit;

@RestController
@RequestMapping("/conversions")
@CrossOrigin(origins = "http://localhost:3001")
/**
 * Controller for conversion-related helper endpoints.
 */
public class ConversionController {

    private static final Logger logger = LoggerFactory.getLogger(ConversionController.class);

    @GetMapping("/all")
    /**
     * List all valid unit conversions in the system.
     */
    public List<String> getAllConversions() {
        logger.info("GET /conversions/all called");
        List<String> list = new ArrayList<>();

        for (Unit from : Unit.values()) {
            for (Unit to : Unit.values()) {
                if (from.getType() == to.getType() && from != to) {
                    list.add(from + " -> " + to);
                }
            }
        }
        return list;
    }

    @GetMapping("/from/{fromUnit}/to/{toUnit}")
    public boolean validateConversion(
            @PathVariable Unit fromUnit,
            @PathVariable Unit toUnit) {

        boolean valid = fromUnit.getType() == toUnit.getType();
        logger.info("GET /conversions/from/{}/to/{} called, valid={}", fromUnit, toUnit, valid);
        return valid;
    }
}
