package com.implemica.selenium.helpers;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@Builder
public class CarValue {
    public CarBrand brand;
    public String model;
    public CarBodyType bodyType;
    public CarTransmissionType transmissionType;
    public String engine;
    public String year;
    public String imageName;
    public String shortDescription;
    public String description;
    public List<String> options;
}
