package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.exceptions.CarNotFoundException;
import com.implemica.model.exceptions.DeleteFileException;
import com.implemica.model.exceptions.StorageServiceException;
import com.implemica.model.repository.CarRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents an implementation of the {@link CarService} interface, which provides a set of methods
 * for managing car entities in the database.
 *
 * @see Car
 * @see CarRepository
 * @see CarService
 * @see SimpleStorageServiceAWS
 */
@Service
public class CarServiceImpl implements CarService {
    /**
     * The storage service used for handling car images in the S3 bucket.
     */
    @Autowired
    private StorageService storageService;

    /**
     * The car repository used for accessing and modifying car objects in the database.
     */
    @Autowired
    private CarRepository carRepository;

    /**
     * The default image path for a car image in case an image is not uploaded.
     */
    @Value("${application.default.image}")
    private String defaultImagePath;

    /**
     * Gets the {@link CarDTO} parameter the car object that will be saved in the car table of the database.
     * Returns the {@link Car} object that was saved.
     *
     * @param carDTO will be saved in the table of cars.
     * @return saved {@link Car}.
     * @throws DataIntegrityViolationException if {@link Car} object already exist table of the database.
     * @see CarDTO
     * @see Car
     */
    @Override
    @CacheEvict(value = "carsList", allEntries = true)
    public Car saveCar(CarDTO carDTO) {
        Car car = getCarFromCarDto(carDTO);
        car.setImageName(defaultImagePath);

        return carRepository.save(car);
    }

    /**
     * Gets the {@code id} parameter the car object that will be deleted in the car table of the database.
     *
     * @param id {@link Car} will be deleted.
     * @throws CarNotFoundException if car was not found in the database.
     * @throws DeleteFileException  if car image was not deleted from storage.
     * @see Car
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public void deleteCarById(Long id) throws CarNotFoundException, DeleteFileException {
        String imageName = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found in database"))
                .getImageName();

        deleteCarImage(imageName);
        carRepository.deleteById(id);
    }

    /**
     * Gets the id parameter the car object that will be found in the car table of the database, and sort options alphabetically.
     *
     * @param id {@link Car} will be found.
     * @return the {@link Car} object that was found.
     * @throws CarNotFoundException if {@link Car} not found in table of the database.
     * @see Car
     */
    @Override
    @Cacheable(key = "#id", value = "cars", unless = "#result == null")
    public Car findCarById(Long id) throws CarNotFoundException {
        Car foundCar = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found in database"));
        Collections.sort(foundCar.getOptionsList());

        return foundCar;
    }

    /**
     * Found all cars in the car table of the database.
     *
     * @return {@link List} of found {@code cars}.
     */
    @Override
    @Cacheable(value = "carsList", unless = "#result == null")
    public List<Car> findAll() {
        return carRepository.findAllByOrderByIdDesc();
    }

    /**
     * Gets the image and save in the storage. Update {@code imageName} for {@link Car} by id in the car table of the database.
     *
     * @param image will be saved in storage for {@link Car}.
     * @param id    {@link Car} imageName from the car table will be updated.
     * @throws CarNotFoundException    if {@link Car} not found in table of the database.
     * @throws StorageServiceException if the old car image has not been removed from the storage,
     *                                 or the new car image has not been uploaded to the storage.
     * @see MultipartFile
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public void uploadImageCarById(Long id, MultipartFile image) throws CarNotFoundException, StorageServiceException {
        Car foundCar = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found in database"));
        String imageName = foundCar.getImageName();

        deleteCarImage(imageName);
        foundCar.setImageName(storageService.uploadFile(image));

        carRepository.save(foundCar);
    }

    /**
     * Gets the {@link CarDTO} parameter the car object that will be updated by {@code id} in the car table of the database.
     * Returns the {@link Car} object that was updated.
     *
     * @param carDTO will be updated in the table of cars.
     * @param id     {@link Car} from the car table will be updated.
     * @return updated {@link Car}.
     * @throws DataIntegrityViolationException if {@link Car} object already exist table of the database.
     * @throws CarNotFoundException            if {@link Car} not found in table of the database.
     * @see CarDTO
     * @see Car
     */
    @Override
    @CachePut(key = "#id", value = "cars")
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public Car updateCarById(Long id, CarDTO carDTO) throws CarNotFoundException {
        Car foundCar = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found in database"));

        Car car = getCarFromCarDto(carDTO);
        car.setImageName(foundCar.getImageName());
        car.setId(foundCar.getId());

        return carRepository.save(car);
    }

    /**
     * This method maps a {@link CarDTO} to a {@link Car} entity.
     *
     * @param carDTO the car DTO to be mapped.
     * @return a {@link Car} entity that was created from the provided {@link CarDTO} data.
     * @throws IllegalArgumentException if the provided {@link CarDTO} has null or invalid data.
     */
    private Car getCarFromCarDto(CarDTO carDTO) {
        Car car = new Car();

        car.setBrand(CarBrand.valueOf(carDTO.getBrand()));
        car.setBodyType(CarBodyType.valueOf(carDTO.getBodyType()));
        car.setYear(carDTO.getYear());
        car.setTransmissionType(CarTransmissionType.valueOf(carDTO.getTransmissionType()));
        car.setEngineSize(carDTO.getEngineSize());
        car.setOptionsList(carDTO.getOptionsList());
        car.setModel(carDTO.getModel());
        car.setDescription(carDTO.getDescription());
        car.setShortDescription(carDTO.getShortDescription());

        return car;
    }

    private void deleteCarImage(String imageName) throws DeleteFileException {
        if (!imageName.equals(defaultImagePath)) {
            storageService.deleteFile(imageName);
        }
    }
}
