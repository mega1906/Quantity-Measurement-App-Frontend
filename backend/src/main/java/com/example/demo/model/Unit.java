package com.example.demo.model;

/**
 * Unit enum defines available units and base conversion factors.
 *
 * For non-temperature types this enum uses linear scaling via toBase/fromBase.
 * Temperature conversions are handled separately in the service layer.
 */
public enum Unit {
    // Length
    METER(MeasurementType.LENGTH, 1.0),
    MILLIMETER(MeasurementType.LENGTH, 0.001),
    CENTIMETER(MeasurementType.LENGTH, 0.01),
    INCH(MeasurementType.LENGTH, 0.0254),
    FOOT(MeasurementType.LENGTH, 0.3048),
    KILOMETER(MeasurementType.LENGTH, 1000.0),
    MILE(MeasurementType.LENGTH, 1609.344),

    // Weight
    MILLIGRAM(MeasurementType.WEIGHT, 0.001),
    GRAM(MeasurementType.WEIGHT, 1.0),
    KILOGRAM(MeasurementType.WEIGHT, 1000.0),
    OUNCE(MeasurementType.WEIGHT, 28.349523125),
    POUND(MeasurementType.WEIGHT, 453.59237),

    // Volume
    LITER(MeasurementType.VOLUME, 1.0),
    MILLILITER(MeasurementType.VOLUME, 0.001),
    CUP(MeasurementType.VOLUME, 0.2365882365),
    GALLON(MeasurementType.VOLUME, 3.785411784),

    // Temperature 
    CELSIUS(MeasurementType.TEMPERATURE, 1.0),
    FAHRENHEIT(MeasurementType.TEMPERATURE, 1.0),
    KELVIN(MeasurementType.TEMPERATURE, 1.0);

    private final MeasurementType type;
    private final double toBaseFactor;

    Unit(MeasurementType type, double toBaseFactor) {
        this.type = type;
        this.toBaseFactor = toBaseFactor;
    }

    public MeasurementType getType() {
        return type;
    }

    public double toBase(double value) {
        return value * toBaseFactor;
    }

    public double fromBase(double baseValue) {
        return baseValue / toBaseFactor;
    }
}
