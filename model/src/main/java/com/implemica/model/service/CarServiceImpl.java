package com.implemica.model.service;

import com.implemica.model.entity.Car;
import com.implemica.model.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService{
    @Autowired
    CarRepository carRepository;

    @Override
    public void saveCar(Car car) {
        carRepository.save(car);
    }

    @Override
    public boolean deleteCarById(Long id) {
        if(carRepository.existsById(id)){
            carRepository.deleteById(id);
            return false;
        }
        return true;
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Car> findAll(){
        return (List<Car>) carRepository.findAll();
    }

    @Override
    public boolean update(Car car, long id) {
        if (carRepository.existsById(id)) {
            car.setId(id);
            carRepository.save(car);
            return true;
        }
        return false;
    }
}
