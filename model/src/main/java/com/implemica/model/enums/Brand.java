package com.implemica.model.enums;

public enum Brand {
    AUDI("Audi"), ACURA("Acura"), ALFA("Alfa"), ROMEO("Romeo"),
    ASTON_MARTIN("Aston Martin"), BENTLEY("Bentley"), BYD("BYD"), BMW("BMW"),
    BRILLIANCE("Brilliance"), BUICK("Buick"), BUGATTI("Bugatti"),
    CADILLAC("Cadillac"), CHANGAN("Changan"), CHEVROLET("Chevrolet"),
    CHERY("Chery"), CHRYSLER("Chrysler"), CITROEN("Citroen"), DAEWOO("Daewoo"),
    DACIA("Dacia"), DAIHATSU("Daihatsu"), DODGE("Dodge"), FAW("FAW"),
    FERRARI("Ferrari"), FIAT("Fiat"), FORD("Ford"), GEELY("Geely"),
    GMC("GMC"), GREAT_WALL("Great_wall"), HONDA("Honda"), HUMMER("Hummer"),
    HYUNDAI("Hyundai"), INFINITI("Infiniti"), JAGUAR("Jaguar"), JEEP("Jeep"),
    KIA("Kia"), LAMBORGHINI("Lamborghini"), LAND_ROVER("Land_rover"),
    LANCIA("Lancia"), LEXUS("Lexus"), LIFAN("Lifan"), LINCOLN("Lincoln"),
    LOTUS("Lotus"), MARUSSIA("Marussia"), MAYBACH("Maybach"), MAZDA("Mazda"),
    MERCEDES("Mercedes"), MASERATI("Maserati"), MINI("Mini"), MCLAREN("Mclaren"),
    MITSUBISHI("Mitsubishi"), NISSAN("NISSAN"), OPEL("OPEL"), PEUGEOT("PEUGEOT"),
    PORSCHE("Porsche"), RENAULT("RENAULT"), SAAB("SAAB"), SEAT("SEAT"),
    SKODA("Skoda"), SUBARU("SUBARU"), SUZUKI("SUZUKI"), TOYOTA("TOYOTA"),
    PONTIAC("Pontiac"), ROLLS_ROYCE("ROLLS_ROYCE"), SMART("SMART"),
    SSANGYONG("Sangyong"), TESLA("Tesla"), VOLVO("Volvo"), DATSUN("Datsun"),
    VOLKSWAGEN("Volkswagen"), TAGAZ("Tagaz"), HAVAL_ROVER("Haval_Rover"),
    GENESIS("Genesis");

    private final String stringValue;

    Brand(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
