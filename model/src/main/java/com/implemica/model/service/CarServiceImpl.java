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
    StorageService storageService;

    @Autowired
    CarRepository carRepository;

    @Value("${application.default.image}")
    String defaultImagePath;

    @Override
    @CacheEvict(value = "carsList", allEntries = true)
    public Car saveCar(CarDTO carDto) {
        Car car = getCarFromCarDto(carDto);

        car.setImageName(defaultImagePath);
        return carRepository.save(car);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "carsList", allEntries = true), @CacheEvict(key = "#id", value = "cars")})
    public boolean deleteCarById(Long id) {
        Optional<Car> foundCar = carRepository.findById(id);

        if (foundCar.isPresent()) {
            String imageName = foundCar.get().getImageName();

            if(storageService.deleteFile(imageName)){
                carRepository.deleteById(id);
                return true;
            }

        }

        return false;
    }

    @Override
    @Cacheable(key = "#id", value = "cars", unless = "#result == null")
    public Car findCarById(Long id) {
        Optional<Car> foundCar = carRepository.findById(id);

        if(foundCar.isPresent()){
            Car car = foundCar.get();
            List<String> optionsList = car.getOptionsList();
            optionsList = optionsList.stream().sorted(String::compareTo).collect(Collectors.toList());
            car.setOptionsList(optionsList);

            return car;
        }

        return null;
    }

    @Override
    @Cacheable(value = "carsList", unless = "#result == null")
    public List<Car> findAll() {
        return carRepository.findAllByOrderByIdDesc();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "carsList", allEntries = true), @CacheEvict(key = "#id", value = "cars")})
    public boolean uploadImageCarById(Long id, MultipartFile newImage) {
        Optional<Car> foundCar = carRepository.findById(id);

        if(foundCar.isPresent()) {
            Car oldCar = foundCar.get();
            String oldImageName = oldCar.getImageName();

            if(storageService.deleteFile(oldImageName)){
                String newImageName = storageService.uploadFile(newImage);
                oldCar.setImageName(newImageName);
                carRepository.save(oldCar);

                return true;
            }

        }

        return false;
    }

    @Override
    @CachePut(key = "#id", value = "cars")
    @Caching(evict = {@CacheEvict(value = "carsList", allEntries = true), @CacheEvict(key = "#id", value = "cars")})
    public Car updateCarById(Long id, CarDTO carDto) {
        Optional<Car> foundCar = carRepository.findById(id);

        if(foundCar.isPresent()){
            Car car = getCarFromCarDto(carDto);
            Car oldCar = foundCar.get();

            car.setImageName(oldCar.getImageName());
            car.setId(oldCar.getId());

            return carRepository.save(car);
        }

        return null;
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
