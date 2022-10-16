package com.implemica.model.service;

import com.implemica.model.entity.Car;

import java.util.List;

public interface CarService {
    void saveCar(Car car);

    boolean deleteCarById(Long id);

    Car findCarById(Long id);

    List<Car> findAll();

    boolean update(Car car, long id);
}
