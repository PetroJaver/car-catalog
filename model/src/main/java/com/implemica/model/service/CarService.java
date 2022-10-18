package com.implemica.model.service;

import com.implemica.model.dto.CarDto;
import com.implemica.model.entity.Car;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CarService {
    void saveCar(CarDto car) throws IOException, ParseException;

    boolean deleteCarById(Long id);

    Car findCarById(Long id);

    List<Car> findAll();

    boolean update(CarDto car, long id) throws ParseException,IOException;
}
