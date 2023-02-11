package com.implemica.model.repository;

import com.implemica.model.entity.Car;
import com.implemica.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * This repository is an interface that lets you perform various operations involving {@link User} objects.
 * It gets these operations by extending the {@link CrudRepository} interface that is defined in Spring Data Commons.
 *
 * @see CrudRepository
 * @see Car
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find {@link User} by username.
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);
}
