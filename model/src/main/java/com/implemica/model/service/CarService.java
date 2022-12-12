package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService {
    Car saveCar(CarDTO carDto);

    boolean deleteCarById(Long id);

    Car findCarById(Long id);

    List<Car> findAll();

    boolean updateCarById(Long id, CarDTO carDto);

    boolean uploadImageCarById(Long id, MultipartFile image);
}
