package com.implemica.model.entity;

import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.enums.CarBodyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "car")
@ApiModel(description = "Car entity in data base.")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "Unique vehicle id in the database.", example = "1")
    private Long id;

    @Column(name = "image_name")
    @ApiModelProperty(value = "Image name for car. Download a image, " +
            "you need to follow the link https://carcatalogimages.s3.eu-west-3.amazonaws.com/ + imageName.", example = "defaultImageCar.png")
    private String imageName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true,example = "PORSCHE")
    private CarBrand brand;

    @Column(nullable = false, length = 40)
    @ApiModelProperty(example = "911",required = true)
    private String model;

    @Column(name = "body_type")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true,example = "COUPE")
    private CarBodyType bodyType;

    @Column(nullable = false)
    @ApiModelProperty(example = "2018")
    private Integer year;

    @Column(name = "transmission_type")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true,example = "AUTOMATIC")
    private CarTransmissionType transmissionType;

    @Column(scale = 1)
    @ApiModelProperty(example = "4.4",notes = "When engine is 0, it mean" +
            " car is electric")
    private double engineSize;

    @Column(name = "short_description", nullable = true)
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
            " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
    private String shortDescription;

    @Column(length = 10000, nullable = true)
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                    " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                    " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                    " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
    private String description;

    @ElementCollection
    @Column(name = "options_list")
    @ApiModelProperty(example = "[\"Any first option\",\"Any second option\"]")
    private List<String> optionsList;
}
