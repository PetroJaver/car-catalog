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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private StorageService storageService;

    @Autowired
    private CarRepository carRepository;

    @Value("${application.default.image}")
    private String defaultImagePath;


    @Override
    @CacheEvict(value = "carsList", allEntries = true)
    public Car saveCar(CarDTO carDto) {
        Car car = getCarFromCarDto(carDto);
        car.setImageName(defaultImagePath);

        return carRepository.save(car);
    }

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

    @Override
    @Cacheable(value = "carsList", unless = "#result == null")
    public List<Car> findAll() {
        return carRepository.findAllByOrderByIdDesc();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "carsList", allEntries = true),
            @CacheEvict(key = "#id", value = "cars")
    })
    public boolean uploadImageCarById(Long id, MultipartFile newImage) {
        Car foundCar = carRepository.findById(id).orElse(null);
        boolean isImageUploaded = false;

        if (foundCar != null) {
            String foundCarImageName = foundCar.getImageName();

            if (storageService.deleteFile(foundCarImageName)) {
                String newImageName = storageService.uploadFile(newImage);
                foundCar.setImageName(newImageName);
                carRepository.save(foundCar);

                isImageUploaded = true;
            }
        }

        return isImageUploaded;
    }

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
