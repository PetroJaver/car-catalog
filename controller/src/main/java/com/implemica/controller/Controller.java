package com.implemica.controller;

import com.implemica.model.dto.CarDto;
import com.implemica.model.entity.Car;
import com.implemica.model.service.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    CarServiceImpl carService;

    @Value("${upload.path}")
    private String imagesPath;


    @PostMapping(value = "/addCar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addCar(@ModelAttribute() CarDto carDto) {
        if(carDto.getFile().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        try{
            carService.saveCar(carDto);
        }catch (IOException|ParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/cars")
    public ResponseEntity<List<Car>> read() throws IOException{
        final List<Car> cars = carService.findAll();

        return cars != null &&  !cars.isEmpty()
                ? new ResponseEntity<>(cars, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/cars/{id}")
    public ResponseEntity<Car> read(@PathVariable(name = "id") long id) throws IOException {
        final Car car = carService.findCarById(id);

        return car != null
                ? new ResponseEntity<>(car, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/cars/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @ModelAttribute() CarDto carDto) throws IOException,ParseException{
        final boolean updated = carService.update(carDto, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/cars/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = carService.deleteCarById(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(
            value = "/car/image/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable(name = "id") long id) throws IOException {
        String imageName = imagesPath + "/" + carService.findCarById(id).getImageName();

        return Files.readAllBytes(Paths.get(imageName));
    }
}

