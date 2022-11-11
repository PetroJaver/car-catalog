package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CarService {
    void saveCar(CarDTO car);

    boolean deleteCarById(Long id);

    Car findCarById(Long id);

    List<Car> findAll();

    boolean update(CarDTO car, Long id);
}
