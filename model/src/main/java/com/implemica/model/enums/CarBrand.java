package com.implemica.model.enums;

import lombok.AllArgsConstructor;


/**
 * Enum of the most common car brands.
 */
@AllArgsConstructor
public enum CarBrand {
    AUDI("Audi"), ACURA("Acura"), ALFA("Alfa"), ROMEO("Romeo"),
    ASTON_MARTIN("Aston Martin"), BENTLEY("Bentley"), BYD("BYD"), BMW("BMW"),
    BRILLIANCE("Brilliance"), BUICK("Buick"), BUGATTI("Bugatti"),
    CADILLAC("Cadillac"), CHANGAN("Changan"), CHEVROLET("Chevrolet"),
    CHERY("Chery"), CHRYSLER("Chrysler"), CITROEN("Citroen"), DAEWOO("Daewoo"),
    DACIA("Dacia"), DAIHATSU("Daihatsu"), DODGE("Dodge"), FAW("FAW"),
    FERRARI("Ferrari"), FIAT("Fiat"), FORD("Ford"), GEELY("Geely"),
    GMC("GMC"), GREAT_WALL("Great wall"), HONDA("Honda"), HUMMER("Hummer"),
    HYUNDAI("Hyundai"), INFINITI("Infiniti"), JAGUAR("Jaguar"), JEEP("Jeep"),
    KIA("Kia"), LAMBORGHINI("Lamborghini"), LAND_ROVER("Land rover"),
    LANCIA("Lancia"), LEXUS("Lexus"), LIFAN("Lifan"), LINCOLN("Lincoln"),
    LOTUS("Lotus"), MARUSSIA("Marussia"), MAYBACH("Maybach"), MAZDA("Mazda"),
    MERCEDES("Mercedes"), MASERATI("Maserati"), MINI("Mini"), MCLAREN("Mclaren"),
    MITSUBISHI("Mitsubishi"), NISSAN("Nissan"), OPEL("Opel"), PEUGEOT("Peugeot"),
    PORSCHE("Porsche"), RENAULT("Renault"), SAAB("SAAB"), SEAT("SEAT"),
    SKODA("Skoda"), SUBARU("Subaru"), SUZUKI("Suzuki"), TOYOTA("Toyota"),
    PONTIAC("Pontiac"), ROLLS_ROYCE("Rolls-Royce"), SMART("Smart"),
    SANGYONG("Sangyong"), TESLA("Tesla"), VOLVO("Volvo"), DATSUN("Datsun"),
    VOLKSWAGEN("Volkswagen"), TAGAZ("Tagaz"),
    GENESIS("Genesis");

    /**
     * Real name of brand car.
     */
    public final String stringValue;
}
