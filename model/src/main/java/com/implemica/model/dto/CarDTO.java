package com.implemica.model.dto;

import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.enums.CarsBodyType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CarDTO {
    private MultipartFile file;

    private String brand;

    private String model;

    private CarsBodyType bodyType;

    private Integer year;

    private CarTransmissionType transmissionType;

    private double engineSize;

    private String shortDescription;

    private String description;

    private List<String> optionsList;
}
