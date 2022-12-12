package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    StorageService storageService;

    @Autowired
    CarRepository carRepository;

    @Value("${default.image}")
    String defaultImagePath;

    @Override
    public Car saveCar(CarDTO carDto) {
        Car car = getCarFromCarDto(carDto);
        car.setImageName(defaultImagePath);
        return carRepository.save(car);
    }

    @Override
    public boolean deleteCarById(Long id) {
        if (carRepository.existsById(id)) {
            String imageName = carRepository.findById(id).orElseThrow().getImageName();

            if (!imageName.equals(defaultImagePath))
                storageService.deleteFile(imageName);

            carRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public List<Car> findAll() {
        return (List<Car>) carRepository.findAll();
    }

    @Override
    public boolean uploadImageCarById(Long id, MultipartFile newImage) {
        Car oldCar = carRepository.findById(id).orElse(null);

        if (oldCar != null) {
            String oldImageName = oldCar.getImageName();

            if (!oldImageName.equals(defaultImagePath)) {
                storageService.deleteFile(oldImageName);
            }

            String newImageName = storageService.uploadFile(newImage);
            oldCar.setImageName(newImageName);
            carRepository.save(oldCar);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCarById(Long id, CarDTO carDto) {
        Car oldCar = carRepository.findById(id).orElse(null);

        if (oldCar != null) {
            Car car = getCarFromCarDto(carDto);
            car.setImageName(oldCar.getImageName());
            car.setId(oldCar.getId());

            carRepository.save(car);
            return true;
        }

        return false;
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
