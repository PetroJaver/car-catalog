package com.implemica.model.entity;

import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.enums.CarBodyType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarBrand brand;

    @Column(nullable = false, length = 40)
    private String model;

    @Column(name = "body_type")
    @Enumerated(EnumType.STRING)
    private CarBodyType bodyType;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "transmission_type")
    @Enumerated(EnumType.STRING)
    private CarTransmissionType transmissionType;

    @Column(scale = 1)
    private double engineSize;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(length = 10000)
    private String description;

    @ElementCollection
    @Column(name = "options_list")
    private List<String> optionsList;
}
