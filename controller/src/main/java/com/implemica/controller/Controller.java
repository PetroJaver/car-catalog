package com.implemica.controller;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.service.CarServiceImpl;
import com.implemica.model.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/")
@RestController()
public class Controller {

    @Autowired
    CarServiceImpl carService;

    @Autowired
    StorageService storageService;

    @PostMapping(value = "cars", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('cars:create')")
    public ResponseEntity<?> addCar(@ModelAttribute() CarDTO carDto) {
        if (carDto.getFile().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        carService.saveCar(carDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "cars")
    public ResponseEntity<List<Car>> read() {
        final List<Car> cars = carService.findAll();

        return cars != null && !cars.isEmpty() ? new ResponseEntity<>(cars, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "cars/{id}")
    public ResponseEntity<Car> read(@PathVariable(name = "id") long id) {
        final Car car = carService.findCarById(id);

        return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "cars/{id}")
    @PreAuthorize("hasAuthority('cars:update')")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @ModelAttribute() CarDTO carDto) {
        final boolean updated = carService.update(carDto, id);

        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "cars/{id}")
    @PreAuthorize("hasAuthority('cars:delete')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = carService.deleteCarById(id);

        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}

