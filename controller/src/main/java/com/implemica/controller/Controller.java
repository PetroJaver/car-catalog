package com.implemica.controller;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.service.CarServiceImpl;
import com.implemica.model.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/")
@RestController()
public class Controller {
    @Autowired
    CarServiceImpl carService;

    @Autowired
    StorageService storageService;

    @PostMapping(value = "cars", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('cars:create')")
    public ResponseEntity<?> create(@Valid @RequestPart CarDTO carDto, @RequestPart(required = false) MultipartFile file) {
        carService.saveCar(carDto, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "cars")
    public ResponseEntity<List<Car>> getAll() {
        final List<Car> cars = carService.findAll();
        return cars != null && !cars.isEmpty() ? new ResponseEntity<>(cars, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "cars/{id}")
    public ResponseEntity<Car> get(@PathVariable(name = "id") long id) {
        final Car car = carService.findCarById(id);

        return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "cars/{id}")
    @PreAuthorize("hasAuthority('cars:update')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestPart CarDTO carDto, @RequestPart(required = false) MultipartFile file) {
        final boolean updated = carService.update(id, carDto, file);

        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "cars/{id}")
    @PreAuthorize("hasAuthority('cars:delete')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = carService.deleteCarById(id);

        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

