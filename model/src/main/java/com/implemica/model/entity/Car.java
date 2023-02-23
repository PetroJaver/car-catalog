package com.implemica.model.entity;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

/**
 * The Car class represents a car entity in the database.
 * It contains all the necessary fields and their respective
 * annotations for mapping to the database using JPA.
 * It also includes getters and setters for accessing and
 * updating the object's fields.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "car", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueCarConstraint", columnNames = {
                "car_brand", "car_model", "car_body_type", "car_year", "car_transmission_type", "car_engine_size"
        })
})
@ApiModel(description = "Car entity in database.")
public class Car {
    /**
     * The unique id for the car entity in the database.
     */
    @Id
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @ApiModelProperty(value = "Unique vehicle id in the database.", example = "1")
    private Long id;

    /**
     * The name of the image file for the car.
     */
    @Column(name = "car_image_name")
    @ApiModelProperty(value = "Image name for car. Download a image, you need to follow the link <link Amazon Web Service S3 bucket\\ + imageName>.", example = "defaultImageCar.png")
    private String imageName;

    /**
     * The brand of the car.
     */
    @Column(nullable = false, name = "car_brand")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, example = "PORSCHE")
    private CarBrand brand;

    /**
     * The model of the car.
     */
    @Column(nullable = false, length = 40, name = "car_model")
    @ApiModelProperty(example = "911", required = true)
    private String model;

    /**
     * The body type of the car.
     */
    @Column(nullable = false, name = "car_body_type")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, example = "COUPE")
    private CarBodyType bodyType;

    /**
     * The year the car was manufactured.
     */
    @Column(name = "car_year")
    @ApiModelProperty(example = "2018")
    private Integer year;
    /**
     * The type of transmission for the car.
     */
    @Column(name = "car_transmission_type")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, example = "AUTOMATIC")
    private CarTransmissionType transmissionType;
    /**
     * The size of the engine for the car.
     * If the value is 0, it means the car is electric.
     */
    @Column(scale = 1, name = "car_engine_size")
    @ApiModelProperty(example = "4.4", notes = "When engine is 0, it mean car is electric")
    private Double engineSize;
    /**
     * A short description of the car.
     */
    @Column(name = "car_short_description")
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
    private String shortDescription;
    /**
     * A long description of the car.
     */
    @Column(length = 10000, name = "car_description")
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
    private String description;

    /**
     * Specifies a list of options for the car entity. This list is stored as an
     * element collection in the database and is accessed through the optionsList
     * attribute.
     */
    @ElementCollection
    @ApiModelProperty(example = "[\"Any first option\",\"Any second option\"]")
    private List<String> optionsList;
}
