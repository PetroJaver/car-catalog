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
    @Id
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @ApiModelProperty(value = "Unique vehicle id in the database.", example = "1")
    private Long id;

    @Column(name = "car_image_name")
    @ApiModelProperty(value = "Image name for car. Download a image, you need to follow the link <link Amazon Web Service S3 bucket\\ + imageName>.", example = "defaultImageCar.png")
    private String imageName;

    @Column(nullable = false, name = "car_brand")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, example = "PORSCHE")
    private CarBrand brand;

    @Column(nullable = false, length = 40, name = "car_model")
    @ApiModelProperty(example = "911", required = true)
    private String model;

    @Column(nullable = false, name = "car_body_type")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, example = "COUPE")
    private CarBodyType bodyType;

    @Column(name = "car_year")
    @ApiModelProperty(example = "2018")
    private Integer year;

    @Column(name = "car_transmission_type")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, example = "AUTOMATIC")
    private CarTransmissionType transmissionType;

    @Column(scale = 1, name = "car_engine_size")
    @ApiModelProperty(example = "4.4", notes = "When engine is 0, it mean car is electric")
    private Double engineSize;

    @Column(name = "car_short_description")
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
    private String shortDescription;

    @Column(length = 10000, name = "car_description")
    @ApiModelProperty(example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
    private String description;

    @ElementCollection
    @ApiModelProperty(example = "[\"Any first option\",\"Any second option\"]")
    private List<String> optionsList;
}
