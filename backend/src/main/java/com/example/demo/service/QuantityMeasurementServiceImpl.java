package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ArithmeticRequestDto;
import com.example.demo.dto.QuantityDto;
import com.example.demo.dto.QuantityInputDto;
import com.example.demo.model.ComparisonResult;
import com.example.demo.model.OperationType;
import com.example.demo.model.QuantityMeasurementEntity;
import com.example.demo.model.Unit;
import com.example.demo.repository.QuantityMeasurementRepository;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

	private final QuantityMeasurementRepository repository;
	private final QuantityMeasurementCalculationService calculationService;
	private final QuantityMeasurementConversionService conversionService;

	public QuantityMeasurementServiceImpl(
			QuantityMeasurementRepository repository,
			QuantityMeasurementCalculationService calculationService,
			QuantityMeasurementConversionService conversionService) {

		this.repository = repository;
		this.calculationService = calculationService;
		this.conversionService = conversionService;
	}

	/**
	 * Convert quantity and persist operation history.
	 *
	 * @param q source quantity payload
	 * @param targetUnit desired unit for conversion result
	 * @return converted numeric value
	 */
	@Override
	public double convert(QuantityDto q, Unit targetUnit) {
		try {
			if (q.getUnit() == null) {
				throw new IllegalArgumentException("Unit must not be null");
			}

			double result = conversionService.convert(q.getValue(), q.getUnit(), targetUnit);

			// Save every conversion attempt, including success path
			saveHistory(q, null, OperationType.CONVERT, result, targetUnit, false, null);
			return result;

		} catch (Exception ex) {
			// Save failed conversion with error flag and message.
			saveHistory(q, null, OperationType.CONVERT, null, targetUnit, true, ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Compare two quantities and persist comparison history.
	 *
	 * @param input the comparison payload containing two quantity objects
	 * @return human-readable comparison message
	 */
	@Override
	public String compare(QuantityInputDto input) {
		ComparisonResult result = calculationService.compare(input.getThisQuantityDto(), input.getThatQuantityDto());

		String resultString = switch (result) {
			case GREATER -> "GREATER";
			case LESSER  -> "LESSER";
			case EQUAL   -> "EQUAL";
		};

		// Save comparison operation in history (non-error)
		saveHistory(input.getThisQuantityDto(), input.getThatQuantityDto(), OperationType.COMPARE, resultString, null, false, null);

		// API return value is deliberately short and consistent with expectation
		return "THIS quantity is " + resultString + " than THAT quantity";
	}

	@Override
	public List<QuantityMeasurementEntity> getHistoryByOperation(OperationType operation) {
		// Fetch history records by operation name from repository
		return repository.findByOperation(operation.name());
	}

	/**
	 * Persist a history record for every operation with the full trace state.
	 */
	private void saveHistory(
			QuantityDto thisDto,
			QuantityDto thatDto,
			OperationType operation,
			Object result,
			Unit resultUnit,
			boolean isError,
			String errorMessage) {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

		// THIS
		entity.setThisValue(thisDto.getValue());
		if (thisDto.getUnit() != null) {
			entity.setThisUnit(thisDto.getUnit().name());
			entity.setThisMeasurementType(thisDto.getUnit().getType().name());
		} else {
			entity.setThisUnit(null);
			entity.setThisMeasurementType(null);
		}

		// THAT (optional)
		if (thatDto != null) {
			entity.setThatValue(thatDto.getValue());
			entity.setThatUnit(thatDto.getUnit().name());
			entity.setThatMeasurementType(thatDto.getUnit().getType().name()
					);
		}

		// OPERATION
		entity.setOperation(operation.name());

		// RESULT HANDLING
		if (result instanceof Double) {
			entity.setResultValue((Double) result);
			Unit storedResultUnit = resultUnit != null ? resultUnit : thisDto.getUnit();
			entity.setResultUnit(storedResultUnit.name());
			entity.setResultMeasurementType(storedResultUnit.getType().name());
		} else if (result instanceof String) {
			entity.setResultString((String) result);
		}

		// ERROR
		entity.setIsError(isError);
		entity.setErrorMessage(errorMessage);

		repository.save(entity);
	}

	@Override
	public double arithmetic(ArithmeticRequestDto request) {

		double result = calculationService.calculate(request.getThisQuantity(),request.getThatQuantity(),request.getOperation(),request.getResultUnit());

		saveHistory(
				request.getThisQuantity(),
				request.getThatQuantity(),
				OperationType.valueOf(request.getOperation().name()),
				result,
				request.getResultUnit(),
				false,
				null);

		return result;
	}
}
