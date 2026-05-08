package com.example.demo.model;

/**
 * Operation types used for history tracking and filtering.
 */
public enum OperationType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    COMPARE,
    CONVERT;
	
    public String getDisplayName() {
        return this.name().toLowerCase();
    }
}