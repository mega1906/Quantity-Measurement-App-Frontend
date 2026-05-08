package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.QuantityMeasurementEntity;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    // All successful operations
	List<QuantityMeasurementEntity> findByIsErrorFalse();

    // All errored operations
    List<QuantityMeasurementEntity> findByIsErrorTrue();

    // Filter by Operation
    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByOperationAndIsErrorFalse(String operation);

    List<QuantityMeasurementEntity> findByOperationAndIsErrorTrue(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);
    
    // Count
    long countByOperation(String operation);
    long countByOperationAndIsErrorTrue(String operation);

    // FIlter by measurement type
    @Query("""
        SELECT q
        FROM QuantityMeasurementEntity q
        WHERE q.thisMeasurementType = :type
        OR q.thatUnit = :type""")
    List<QuantityMeasurementEntity> findByMeasurementType(
            @Param("type") String type);

    // Recent History
    @Query("""
        SELECT q
        FROM QuantityMeasurementEntity q
        ORDER BY q.createdAt DESC
    """)
    List<QuantityMeasurementEntity> findLatest();
}