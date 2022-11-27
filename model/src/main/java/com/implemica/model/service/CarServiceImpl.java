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
    public void saveCar(CarDTO carDto, MultipartFile file) {
        String imageName;
        if(file == null){
            imageName = defaultImagePath;
        }else{
            imageName = storageService.uploadFile(file);
        }

        carRepository.save(getCarFromCarDto(carDto, imageName));
    }

    @Override
    public boolean deleteCarById(Long id) {
        if (carRepository.existsById(id)) {
            String imageName = carRepository.findById(id).orElseThrow().getImageName();
            if(!imageName.equals(defaultImagePath)){
                storageService.deleteFile(imageName);
            }

            carRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Car findCarById(Long id) {
        Car car;
        try {
            car = carRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            car = null;
        }

        return car;
    }

    @Override
    public List<Car> findAll() {

        return (List<Car>) carRepository.findAll();
    }

    @Override
    public boolean update(Long id, CarDTO carDto, MultipartFile file) {

        if (carRepository.existsById(id)) {
            String imageName = carRepository.findById(id).orElseThrow().getImageName();

            if (file != null) {
                if(!imageName.equals(defaultImagePath)){
                    storageService.deleteFile(imageName);
                }

                imageName = storageService.uploadFile(file);
            }

            Car car = getCarFromCarDto(carDto, imageName);
            car.setId(id);

            carRepository.save(car);
            return true;
        }
        return false;
    }

    private Car getCarFromCarDto(CarDTO carDto, String imageName) {
        Car car = new Car();

        car.setBrand(CarBrand.valueOf(carDto.getBrand()));
        car.setBodyType(CarBodyType.valueOf(carDto.getBodyType()));
        car.setYear(carDto.getYear());
        car.setTransmissionType(CarTransmissionType.valueOf(carDto.getTransmissionType()));
        car.setEngineSize(carDto.getEngineSize());
        car.setDescription(carDto.getDescription());
        car.setOptionsList(carDto.getOptionsList());
        car.setImageName(imageName);
        car.setShortDescription(carDto.getShortDescription());
        car.setModel(carDto.getModel());

        return car;
    }
}
