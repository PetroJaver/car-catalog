package com.implemica.model.repository;

import com.implemica.model.entity.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car,Long> {
    List<Car> findAllByOrderById();
}
