package com.implemica.controller;

import com.implemica.model.entity.Car;
import com.implemica.model.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    CarService carService;

    @PostMapping("/addCar")
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        carService.saveCar(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/cars")
    public ResponseEntity<List<Car>> read() {
        final List<Car> cars = carService.findAll();

        return cars != null &&  !cars.isEmpty()
                ? new ResponseEntity<>(cars, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/cars/{id}")
    public ResponseEntity<Car> read(@PathVariable(name = "id") long id) {
        final Car car = carService.findCarById(id);

        return car != null
                ? new ResponseEntity<>(car, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/cars/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody Car car) {
        final boolean updated = carService.update(car, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/cars/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = carService.deleteCarById(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}

