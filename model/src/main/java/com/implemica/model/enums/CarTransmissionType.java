package com.implemica.model.enums;

public enum CarTransmissionType {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");

    public final String stringValue;

    CarTransmissionType(String stringValue) {
        this.stringValue = stringValue;
    }
}
