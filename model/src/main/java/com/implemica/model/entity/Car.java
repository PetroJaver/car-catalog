package com.implemica.model.entity;

import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.enums.CarsBodyType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(nullable = false, length = 20)
    private String brand;

    @Column(nullable = false, length = 40)
    private String model;

    @Column(name = "body_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarsBodyType bodyType;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "transmission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarTransmissionType transmissionType;

    @Column(scale = 1, nullable = false)
    private double engineSize;

    @Column(length = 10000, nullable = false)
    private String description;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @ElementCollection
    @Column(name = "options_list", nullable = false)
    private List<String> optionsList;
}
