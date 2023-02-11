package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * This is a specification of how a service layer should be implemented for logic working with the {@link Car} entity.
 *
 * @see Car
 * @see CarDTO
 */
public interface CarService {

    /**
     * Presents a contract method that must save the car in the database.
     */
    Car saveCar(CarDTO carDto);

    /**
     * Presents a contract method that must delete the car in the database.
     */
    boolean deleteCarById(Long id);

    /**
     * Presents a contract method that must find the car in the database.
     */
    Car findCarById(Long id);

    /**
     * Presents a contract method that must find all the cars in the database.
     */
    List<Car> findAll();

    /**
     * Presents a contract method that must update the car in the database.
     */
    Car updateCarById(Long id, CarDTO carDto);

    /**
     * Presents a contract method that must upload the image of the car in the database.
     */
    boolean uploadImageCarById(Long id, MultipartFile image);
}
