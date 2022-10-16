package com.implemica.model.entity;

import com.implemica.model.entity.enums.CarTransmissionType;
import com.implemica.model.entity.enums.CarsBodyType;
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
    private long id;

    @Column(nullable = false, length = 20)
    private String brand;

    @Column(name = "body_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarsBodyType bodyType;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private java.util.Date year;

    @Column(name = "transmission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarTransmissionType transmissionType;

    @Column(scale = 1, nullable = false)
    private double engineSize;

    @Column(length = 10000, nullable = false)
    private String description;

    @ElementCollection
    @Column(name = "options_list", nullable = false)
    private List<String> optionsList;
}
