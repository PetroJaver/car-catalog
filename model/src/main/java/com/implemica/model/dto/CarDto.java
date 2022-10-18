package com.implemica.model.dto;

import com.implemica.model.entity.enums.CarTransmissionType;
import com.implemica.model.entity.enums.CarsBodyType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CarDto {
    private MultipartFile file;

    private String brand;

    private CarsBodyType bodyType;

    private String year;

    private CarTransmissionType transmissionType;

    private double engineSize;

    private String description;

    private List<String> optionsList;
}
