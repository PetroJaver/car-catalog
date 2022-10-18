package com.implemica.model.service;

import com.implemica.model.dto.CarDto;
import com.implemica.model.entity.Car;
import com.implemica.model.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class CarServiceImpl implements CarService{
    @Autowired
    CarRepository carRepository;
    @Value("${upload.path}")
    private String imagesPath;

    public void saveCar(CarDto carDto) throws IOException, ParseException {
        MultipartFile file = carDto.getFile();
        String imageName = saveImage(file);
        carRepository.save(getCarFromCarDto(carDto,imageName));
    }

    @Override
    public boolean deleteCarById(Long id) {
        if(carRepository.existsById(id)){
            String imageName = carRepository.findById(id).orElseThrow().getImageName();
            deleteImage(imageName);

            carRepository.deleteById(id);
            return false;
        }

        return true;
    }

    @Override
    public Car findCarById(Long id){

        return carRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Car> findAll(){

        return (List<Car>) carRepository.findAll();
    }


    @Override
    public boolean update(CarDto carDto, long id) throws IOException,ParseException{
        if (carRepository.existsById(id)) {
            String imageName = carRepository.findById(id).orElseThrow().getImageName();
            deleteImage(imageName);
            String newImageName = saveImage(carDto.getFile());

            Car car = getCarFromCarDto(carDto,newImageName);
            car.setId(id);

            carRepository.save(car);
            return true;
        }
        return false;
    }

    private Car getCarFromCarDto(CarDto carDto, String imageName) throws ParseException{
        Car car = new Car();

        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd-mm-yyyy");

        car.setBrand(carDto.getBrand());
        car.setBodyType(carDto.getBodyType());
        car.setYear(format.parse(carDto.getYear()));
        car.setTransmissionType(carDto.getTransmissionType());
        car.setEngineSize(carDto.getEngineSize());
        car.setDescription(carDto.getDescription());
        car.setOptionsList(carDto.getOptionsList());
        car.setImageName(imageName);

        return car;
    }

    private String saveImage(MultipartFile file) throws IOException{

        File uploadDir = new File(imagesPath);

        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }

        String imageName = UUID.randomUUID().toString();
        imageName += "." + file.getOriginalFilename();
        file.transferTo(new File(imagesPath+"/"+imageName));


        return imageName;
    }

    private boolean deleteImage(String imageName){
        File file = new File(imagesPath+"/"+imageName);

        file.delete();

        return false;
    }
}
