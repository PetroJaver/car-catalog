package com.implemica.model.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    StorageService storageService;

    @Autowired
    CarRepository carRepository;

    @Override
    public void saveCar(CarDTO carDto) throws IOException, ParseException {
        MultipartFile file = carDto.getFile();
        String imageName = storageService.uploadFile(file);
        carRepository.save(getCarFromCarDto(carDto, imageName));
    }

    @Override
    public boolean deleteCarById(Long id) {
        if (carRepository.existsById(id)) {
            String imageName = carRepository.findById(id).orElseThrow().getImageName();
            storageService.deleteFile(imageName);

            carRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Car findCarById(Long id) {

        return carRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Car> findAll() {

        return (List<Car>) carRepository.findAll();
    }


    @Override
    public boolean update(CarDTO carDto, Long id) throws IOException {
        if (carRepository.existsById(id)) {
            String imageName = carRepository.findById(id).orElseThrow().getImageName();
            storageService.deleteFile(imageName);
            String newImageName = storageService.uploadFile(carDto.getFile());

            Car car = getCarFromCarDto(carDto, newImageName);
            car.setId(id);

            carRepository.save(car);
            return true;
        }
        return false;
    }

    private Car getCarFromCarDto(CarDTO carDto, String imageName) {
        Car car = new Car();

        car.setBrand(carDto.getBrand());
        car.setBodyType(carDto.getBodyType());
        car.setYear(carDto.getYear());
        car.setTransmissionType(carDto.getTransmissionType());
        car.setEngineSize(carDto.getEngineSize());
        car.setDescription(carDto.getDescription());
        car.setOptionsList(carDto.getOptionsList());
        car.setImageName(imageName);
        car.setShortDescription(carDto.getShortDescription());
        car.setModel(carDto.getModel());

        return car;
    }
}
