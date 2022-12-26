package com.implemica.model.enums;

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

    public final String stringValue;

    CarBodyType(String stringValue){
        this.stringValue = stringValue;
    }
}

