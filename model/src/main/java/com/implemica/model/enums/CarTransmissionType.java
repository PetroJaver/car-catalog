package com.implemica.model.enums;

import lombok.AllArgsConstructor;

/**
 * Enum of the most common car transmission types.
 */
@AllArgsConstructor
public enum CarTransmissionType {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");


    /**
     * Real name of transmission type car.
     */
    public final String stringValue;
}
