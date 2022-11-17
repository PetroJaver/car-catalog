package com.implemica.application.util.service;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.repository.CarRepository;
import com.implemica.model.service.CarServiceImpl;
import com.implemica.model.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {
    @Mock
    CarRepository carRepository;

    @Mock
    StorageService storageService;

    @InjectMocks
    CarServiceImpl carService;

    private final Car firstCar = new Car(1L, "imgForFirstCar", CarBrand.BMW, "Series 8"
            , CarBodyType.COUPE, 2020, CarTransmissionType.AUTOMATIC, 4d
            , "It is a long established fact that a reader will"
            , "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            , Arrays.asList("Climate control", "Signaling"));

    private final Car secondCar = new Car(2L, "imgForSecondCar", CarBrand.AUDI, "A8", CarBodyType.COUPE
            , 2021, CarTransmissionType.AUTOMATIC, 4.1
            , "It is a long established fact that a reader will"
            , "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            , Arrays.asList("Climate control", "Signaling"));

    private final Car thirdCar = new Car(3L, "imgForThirdCar", CarBrand.MASERATI, "A8", CarBodyType.COUPE
            , 2021, CarTransmissionType.MANUAL, 6.2
            , "It is a long established fact that a reader will"
            , "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            , Arrays.asList("Climate control", "Signaling"));

    private final CarDTO firstCarDto = new CarDTO(CarBrand.BMW.toString(), "Series 8"
            , CarBodyType.COUPE.toString(), 2020, CarTransmissionType.AUTOMATIC.toString(), 4d
            , "It is a long established fact that a reader will"
            , "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            , Arrays.asList("Climate control", "Signaling"));

    private final CarDTO noValidBrandCarDto = new CarDTO("bmw", "Series 8"
            , CarBodyType.COUPE.toString(), 2020, CarTransmissionType.AUTOMATIC.toString(), 4d
            , "It is a long established fact that a reader will"
            , "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            , Arrays.asList("Climate control", "Signaling"));

    private final MultipartFile file = new MockMultipartFile("imgForFirstCar", "Testing file.".getBytes());

    private final CarDTO carDtoSave = new CarDTO(CarBrand.BMW.toString(), "Series 8"
            , CarBodyType.COUPE.toString(), 2020, CarTransmissionType.AUTOMATIC.toString(), 4d
            , "It is a long established fact that a reader will"
            , "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            , Arrays.asList("Climate control", "Signaling"));

    @Test
    public void saveCar() {
        when(storageService.uploadFile(any(MultipartFile.class))).thenReturn("imgForFirstCar");
        when(carRepository.save(any(Car.class))).thenReturn(firstCar);

        carService.saveCar(carDtoSave, file);

        verify(storageService, times(1)).uploadFile(file);
        verify(carRepository, times(1)).save(any(Car.class));
        verifyNoMoreInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteCarExistCar() {
        String imageName = firstCar.getImageName();

        when(carRepository.existsById(1L)).thenReturn(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(firstCar));
        when(storageService.deleteFile(imageName)).thenReturn(true);

        assertTrue(carService.deleteCarById(1L));

        verify(carRepository, times(1)).existsById(1L);
        verify(storageService, times(1)).deleteFile(imageName);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteCarNotExistCar() {
        when(carRepository.existsById(1L)).thenReturn(false);

        assertFalse(carService.deleteCarById(1L));

        verify(carRepository, times(1)).existsById(1L);
        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findCarByIdExistCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(firstCar));
        Car findCar = carService.findCarById(1L);

        assertEquals(firstCar, findCar);

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findCarByIdNotExistCar() {
        when(carRepository.findById(1L)).thenThrow(NoSuchElementException.class);

        Car findCar = carService.findCarById(1L);

        assertEquals(null, findCar);

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void updateCarExistCar() {
        Car existCar = secondCar;
        existCar.setId(1L);
        String imageName = existCar.getImageName();
        String imageNameNew = file.getName();


        when(carRepository.existsById(1L)).thenReturn(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(existCar));
        when(storageService.deleteFile(imageName)).thenReturn(true);
        when(storageService.uploadFile(file)).thenReturn(imageNameNew);

        assertTrue(carService.update(1L, firstCarDto, file));

        verify(carRepository, times(1)).existsById(1L);
        verify(storageService, times(1)).deleteFile(imageName);
        verify(storageService, times(1)).uploadFile(file);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
        verifyNoMoreInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void updateCarFileNull() {
        Car existCar = secondCar;
        existCar.setId(1L);

        when(carRepository.existsById(1L)).thenReturn(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(existCar));

        assertTrue(carService.update(1L, firstCarDto, null));

        verify(carRepository, times(1)).existsById(1L);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void updateCarNotExistCar() {
        when(carRepository.existsById(1L)).thenReturn(false);

        assertFalse(carService.update(1L, firstCarDto, file));

        verify(carRepository, times(1)).existsById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findAll() {
        List<Car> carList = Arrays.asList(firstCar, secondCar, thirdCar);
        when(carRepository.findAll()).thenReturn(carList);

        assertEquals(carService.findAll(), carList);

        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }
}
