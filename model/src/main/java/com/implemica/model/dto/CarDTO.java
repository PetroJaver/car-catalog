package com.implemica.model.dto;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.validators.ValueOfEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

/**
 * A data transfer object used to transfer car data to a server.
 * It is used for creating or updating a car entity in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CarDTO", description = "Car data transfer object used to transfer vehicle data to a server.")
public class CarDTO {
    /**
     * The brand of the car.
     */
    @ValueOfEnum(enumClass = CarBrand.class, message = "Invalid brand")
    @NotNull(message = "brand is null")
    @NotEmpty(message = "brand is empty")
    @ApiModelProperty(required = true, example = "PORSCHE",
            dataType = "com.implemica.model.enums.CarBrand")
    private String brand;

    /**
     * The model of the car.
     */
    @NotNull(message = "model is null")
    @NotEmpty(message = "model is empty")
    @Pattern(regexp = "[a-zA-Z0-9-\\s]*", message = "model is not correct")
    @Size(min = 2, max = 40, message = "model is length not from 2 to 40")
    @ApiModelProperty(example = "Cayenne", required = true)
    private String model;

    /**
     * The body type of the car.
     */
    @ValueOfEnum(enumClass = CarBodyType.class, message = "Invalid bodyType")
    @NotNull(message = "body type is null")
    @NotEmpty(message = "body type is empty")
    @ApiModelProperty(required = true, example = "COUPE",
            dataType = "com.implemica.model.enums.CarBodyType")
    private String bodyType;

    /**
     * The year of the car.
     */
    @Min(value = 1880, message = "year less than 1880")
    @Max(value = 2100, message = "year more than 2100")
    @ApiModelProperty(example = "2018")
    private Integer year;

    /**
     * The transmission type of the car.
     */
    @ValueOfEnum(enumClass = CarTransmissionType.class, message = "Invalid transmissionType")
    @ApiModelProperty(required = true, example = "AUTOMATIC"
            , dataType = "com.implemica.model.enums.CarTransmissionType")
    private String transmissionType;

    /**
     * The engine size of the car.
     * When engine size is 0, it means that the car is electric.
     */
    @DecimalMax(value = "10.0", message = "engineSize more than 10")
    @DecimalMin(value = "0", message = "engineSize less than 0")
    @ApiModelProperty(example = "4.4", notes = "When engine is 0, it mean" +
            " car is electric.")
    private Double engineSize;

    /**
     * A short description of the car.
     */
    @Size(min = 25, max = 150, message = "shortDescription is length not from 25 to 150")
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
            ", sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
    private String shortDescription;

    /**
     * A long description of the car.
     */
    @Size(min = 50, max = 5000, message = "description is length not from 50 to 5000")
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
            " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
            " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
            " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
    private String description;

    /**
     * An options list of the car.
     */
    @ApiModelProperty(example = "[\"Any first option\",\"Any second option\"]")
    private List<@NotNull(message = "option is null")
    @NotEmpty(message = "option is empty")
    @Pattern(regexp = "[a-zA-Z0-9-\\s']*", message = "option is not correct")
    @Size(min = 2, max = 30, message = "option is length not from 2 to 25") String> optionsList;
}
