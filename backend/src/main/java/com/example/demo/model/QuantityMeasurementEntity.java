package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity for persisting all quantity measurement operations.
 * Used ONLY for persistence and audit/history.
 */
@Entity
@Table(
    name = "quantity_measurements",
    indexes = {
        @Index(name = "idx_operation",        columnList = "operation"),
        @Index(name = "idx_measurement_type", columnList = "this_measurement_type"),
        @Index(name = "idx_created_at",       columnList = "created_at")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "this_value", nullable = false)
    private Double thisValue;

    @Column(name = "this_unit", nullable = false)
    private String thisUnit;

    @Column(name = "this_measurement_type", nullable = false)
    private String thisMeasurementType;

    @Column(name = "that_value")
    private Double thatValue;

    @Column(name = "that_unit")
    private String thatUnit;

    @Column(name = "that_measurement_type")
    private String thatMeasurementType;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit")
    private String resultUnit;

    @Column(name = "result_measurement_type")
    private String resultMeasurementType;

    /**
     * Used mainly for comparison results:
     * "GREATER", "LESSER", "EQUAL"
     */
    @Column(name = "result_string")
    private String resultString;

    @Column(name = "is_error", nullable = false)
    private Boolean isError = false;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getThisValue() {
		return thisValue;
	}

	public void setThisValue(Double thisValue) {
		this.thisValue = thisValue;
	}

	public String getThisUnit() {
		return thisUnit;
	}

	public void setThisUnit(String thisUnit) {
		this.thisUnit = thisUnit;
	}

	public String getThisMeasurementType() {
		return thisMeasurementType;
	}

	public void setThisMeasurementType(String thisMeasurementType) {
		this.thisMeasurementType = thisMeasurementType;
	}

	public Double getThatValue() {
		return thatValue;
	}

	public void setThatValue(Double thatValue) {
		this.thatValue = thatValue;
	}

	public String getThatUnit() {
		return thatUnit;
	}

	public void setThatUnit(String thatUnit) {
		this.thatUnit = thatUnit;
	}

	public String getThatMeasurementType() {
		return thatMeasurementType;
	}

	public void setThatMeasurementType(String thatMeasurementType) {
		this.thatMeasurementType = thatMeasurementType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Double getResultValue() {
		return resultValue;
	}

	public void setResultValue(Double resultValue) {
		this.resultValue = resultValue;
	}

	public String getResultUnit() {
		return resultUnit;
	}

	public void setResultUnit(String resultUnit) {
		this.resultUnit = resultUnit;
	}

	public String getResultMeasurementType() {
		return resultMeasurementType;
	}

	public void setResultMeasurementType(String resultMeasurementType) {
		this.resultMeasurementType = resultMeasurementType;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}