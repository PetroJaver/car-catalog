package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService {
    void saveCar(CarDTO carDto, MultipartFile file);

    boolean deleteCarById(Long id);

    Car findCarById(Long id);

    List<Car> findAll();

    boolean update(Long id, CarDTO carDto, MultipartFile file);
}
