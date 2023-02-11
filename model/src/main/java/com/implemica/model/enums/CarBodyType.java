package com.implemica.model.enums;

import lombok.AllArgsConstructor;

/**
 * Enum of the most common car body types.
 */
@AllArgsConstructor
public enum CarBodyType {
    HATCHBACK("Hatchback"),
    SEDAN("Sedan"),
    MUV("MUV"),
    SUV("SUV"),
    COUPE("Coupe"),
    CONVERTIBLE("Convertible"),
    WAGON("Wagon"),
    VAN("Van"),
    JEEP("Jeep"),
    SPORTCAR("Sport car");

    /**
     * Real name of body type car.
     */
    public final String stringValue;
}

