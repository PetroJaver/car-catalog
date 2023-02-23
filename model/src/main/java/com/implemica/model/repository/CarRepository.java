package com.implemica.model.repository;

import com.implemica.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This repository is an interface that lets you perform various operations involving {@link Car} objects.
 * It gets these operations by extending the {@link CrudRepository} interface that is defined in Spring Data Commons.
 *
 * @see CrudRepository
 * @see Car
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    /**
     * Find a list of {@link Car} ordered by {@code id} from bigger to smaller.
     *
     * @return list of found cars.
     */
    List<Car> findAllByOrderByIdDesc();
}
