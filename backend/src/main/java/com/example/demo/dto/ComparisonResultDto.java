package com.example.demo.dto;

import com.example.demo.model.ComparisonResult;

/**
 * DTO used to return comparison result and optional textual message.
 */
public class ComparisonResultDto {

    private ComparisonResult result;
    private String message;

    public ComparisonResultDto() {
    }

    public ComparisonResultDto(ComparisonResult result, String message) {
        this.result = result;
        this.message = message;
    }

    public ComparisonResult getResult() {
        return result;
    }

    public void setResult(ComparisonResult result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
