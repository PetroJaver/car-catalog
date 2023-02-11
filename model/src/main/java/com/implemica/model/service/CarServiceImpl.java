package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviсe layer that works with the car table in the database, to perform the main operations with the {@link Car}.
 *
 * @see Car
 * @see CarRepository
 * @see CarService
 * @see SimpleStorageServiceAWS
 */
@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private StorageService storageService;

    @Autowired
    private CarRepository carRepository;

    @Value("${application.default.image}")
    private String defaultImagePath;

    /**
     * Gets the {@link CarDTO} parameter the car object that will be saved in the car table of the database.
     * Returns the {@link Car} object that was saved.
     *
     * @param carDto will be saved in the table of cars.
     * @return saved {@link Car}.
     * @throws DataIntegrityViolationException if {@link Car} object already exist table of the database.
     * @see CarDTO
     * @see Car
     */
    @Override
    @CacheEvict(value = "carsList", allEntries = true)
    public Car saveCar(CarDTO carDto) {
        Car car = getCarFromCarDto(carDto);
        car.setImageName(defaultImagePath);

        return carRepository.save(car);
    }

    /**
     * Gets the {@code id} parameter the car object that will be deleted in the car table of the database.
     * Returns {@code true} if car will be deleted successful, otherwise {@code false}.
     *
     * @param id {@link Car} will be deleted.
     * @return false, if {@link Car} by id not found in the {@code Car} table.
     * @see Car
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public boolean deleteCarById(Long id) {
        Car foundCar = carRepository.findById(id).orElse(null);
        boolean isCarDeleted = false;

        if (foundCar != null) {
            String imageName = foundCar.getImageName();

            if (storageService.deleteFile(imageName)) {
                carRepository.deleteById(id);

                isCarDeleted = true;
            }
        }

        return isCarDeleted;
    }

    /**
     * Gets the id parameter the car object that will be found in the car table of the database, and sort options alphabetically.
     *
     * @param id {@link Car} will be found.
     * @return the {@link Car} object that was found.
     * @see Car
     */
    @Override
    @Cacheable(key = "#id", value = "cars", unless = "#result == null")
    public Car findCarById(Long id) {
        Car foundCar = carRepository.findById(id).orElse(null);

        if (foundCar != null) {
            List<String> optionsList = foundCar.getOptionsList();

            optionsList = optionsList.stream().sorted(String::compareTo).collect(Collectors.toList());
            foundCar.setOptionsList(optionsList);
        }

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
     * @return false, if {@link Car} by id not found in the {@code Car} table.
     * @see MultipartFile
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public boolean uploadImageCarById(Long id, MultipartFile image) {
        Car foundCar = carRepository.findById(id).orElse(null);
        boolean isImageUploaded = false;

        if (foundCar != null) {
            String foundCarImageName = foundCar.getImageName();

            if (storageService.deleteFile(foundCarImageName)) {
                String newImageName = storageService.uploadFile(image);
                foundCar.setImageName(newImageName);
                carRepository.save(foundCar);

                isImageUploaded = true;
            }
        }

        return isImageUploaded;
    }

    /**
     * Gets the {@link CarDTO} parameter the car object that will be updated by {@code id} in the car table of the database.
     * Returns the {@link Car} object that was updated.
     *
     * @param carDto will be updated in the table of cars.
     * @param id {@link Car} from the car table will be updated.
     * @return updated {@link Car}.
     * @throws DataIntegrityViolationException if {@link Car} object already exist table of the database.
     * @see CarDTO
     * @see Car
     */
    @Override
    @CachePut(key = "#id", value = "cars")
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public Car updateCarById(Long id, CarDTO carDto) {
        Car foundCar = carRepository.findById(id).orElse(null);

        if (foundCar != null) {
            Car car = getCarFromCarDto(carDto);
            car.setImageName(foundCar.getImageName());
            car.setId(foundCar.getId());

            foundCar = carRepository.save(car);
        }

        return foundCar;
    }

    private Car getCarFromCarDto(CarDTO carDto) {
        Car car = new Car();

        car.setBrand(CarBrand.valueOf(carDto.getBrand()));
        car.setBodyType(CarBodyType.valueOf(carDto.getBodyType()));
        car.setYear(carDto.getYear());
        car.setTransmissionType(CarTransmissionType.valueOf(carDto.getTransmissionType()));
        car.setEngineSize(carDto.getEngineSize());
        car.setOptionsList(carDto.getOptionsList());
        car.setModel(carDto.getModel());
        car.setDescription(carDto.getDescription());
        car.setShortDescription(carDto.getShortDescription());

        return car;
    }
}
