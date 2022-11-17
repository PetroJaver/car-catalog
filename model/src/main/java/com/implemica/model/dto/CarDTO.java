package com.implemica.model.dto;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.validators.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    @ValueOfEnum(enumClass = CarBrand.class,message = "Invalid brand")
    private String brand;

    @NotNull(message = "model is null")
    @NotEmpty(message = "model is empty")
    @Pattern(regexp = "[a-zA-Z0-9-\\s]*",message = "model is not correct")
    @Size(min = 2, max=40, message = "model is length not from 2 to 40")
    private String model;

    @ValueOfEnum(enumClass = CarBodyType.class,message = "Invalid bodyType")
    private String bodyType;

    @Min(value = 1880, message = "year less than 1880")
    @Max(value = 2100, message = "year more than 2100")
    private Integer year;

    @ValueOfEnum(enumClass = CarTransmissionType.class,message = "Invalid transmissionType")
    private String transmissionType;

    @DecimalMax(value = "10.0", message = "engineSize less than 0.1")
    @DecimalMin(value = "0.1", message = "engineSize more than 10")
    private double engineSize;

    @Size(min = 25,max = 150,message = "shortDescription is length not from 25 to 150")
    private String shortDescription;

    @Size(min = 50,max = 5000,message = "description is length not from 50 to 5000")
    private String description;

    private List<@NotNull(message = "option is null")
                @NotEmpty(message = "option is empty")
                @Pattern(regexp = "[a-zA-Z0-9-\\s]*",message = "option is not correct")
                @Size(min = 3, max=25, message = "option is length not from 3 to 25") String> optionsList;
}
