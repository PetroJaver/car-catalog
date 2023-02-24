package com.implemica.application.util.service;

import com.implemica.model.entity.Car;
import com.implemica.model.exceptions.CarNotFoundException;
import com.implemica.model.exceptions.DeleteFileException;
import com.implemica.model.repository.CarRepository;
import com.implemica.model.service.CarServiceImpl;
import com.implemica.model.service.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.implemica.application.util.helpers.TestValues.*;
import static org.junit.Assert.assertThrows;
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

    @Before
    public void before() {
        org.springframework.test.util.ReflectionTestUtils.setField(carService, "defaultImagePath", DEFAULT_IMAGE_PATH);
    }

    @Test
    public void saveCar() {
        when(carRepository.save(EXAMPLE_CAR_WITHOUT_ID)).thenReturn(EXAMPLE_CAR);

        Car returnedCar = carService.saveCar(EXAMPLE_CAR_DTO);
        assertEquals(EXAMPLE_CAR, returnedCar);

        verify(carRepository, times(1)).save(any(Car.class));

        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteExistCarWithDefaultImage() throws Exception {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR));
        carService.deleteCarById(1L);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).deleteById(1L);
        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteExistCarWithNotDefaultImage() throws Exception {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME));
        carService.deleteCarById(1L);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).deleteById(1L);
        verify(storageService, times(1)).deleteFile(NOT_DEFAULT_IMAGE_PATH);
        verifyNoMoreInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteCarNotExistCar() throws Exception {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows("Car not found in database", CarNotFoundException.class, () -> carService.deleteCarById(1L));

        verify(carRepository, times(1)).findById(1L);
        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findCarByIdExistCar() throws Exception {
        Optional<Car> returnedCar = Optional.of(cloneCar(EXAMPLE_CAR));
        when(carRepository.findById(1L)).thenReturn(returnedCar);
        Car foundCar = carService.findCarById(1L);

        assertEquals(EXAMPLE_CAR, foundCar);

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findCarByIdNotExistCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows("Car not found in database", CarNotFoundException.class, () -> carService.findCarById(1L));

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void updateCarExistCar() throws Exception {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR));
        when(carRepository.save(EXAMPLE_CAR)).thenReturn(EXAMPLE_CAR);

        assertEquals(carService.updateCarById(1L, EXAMPLE_CAR_DTO), EXAMPLE_CAR);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(EXAMPLE_CAR);
        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void updateCarNotExist() throws Exception {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows("Car not found in database", CarNotFoundException.class, () -> carService.updateCarById(1L, EXAMPLE_CAR_DTO));

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findAllNotEmpty() {
        when(carRepository.findAllByOrderByIdDesc()).thenReturn(CAR_LIST);

        assertEquals(CAR_LIST, carService.findAll());

        verify(carRepository, times(1)).findAllByOrderByIdDesc();
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findAllEmpty() {
        when(carRepository.findAllByOrderByIdDesc()).thenReturn(null);

        assertNull(carService.findAll());

        verify(carRepository, times(1)).findAllByOrderByIdDesc();
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void uploadImageExistCarByIdWithDefaultImage() throws Exception {
        Car car = cloneCar(EXAMPLE_CAR);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME))
                .thenReturn(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        when(storageService.uploadFile(MULTIPART_FILE)).thenReturn(NOT_DEFAULT_IMAGE_PATH);

        carService.uploadImageCarById(1L, MULTIPART_FILE);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        verify(storageService, times(1)).uploadFile(MULTIPART_FILE);
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(storageService);
    }

    @Test
    public void uploadImageExistCarByIdWithNotDefaultImage() throws Exception {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME));
        when(carRepository.save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME))
                .thenReturn(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        when(storageService.uploadFile(MULTIPART_FILE)).thenReturn(NOT_DEFAULT_IMAGE_PATH);

        carService.uploadImageCarById(1L, MULTIPART_FILE);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        verify(storageService, times(1)).uploadFile(MULTIPART_FILE);
        verify(storageService, times(1)).deleteFile(MULTIPART_FILE.getName());
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(storageService);
    }

    @Test
    public void uploadImageNotExistCarById() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows("Car not found in database", CarNotFoundException.class, () -> carService.uploadImageCarById(1L, MULTIPART_FILE));

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    private Car cloneCar(Car car) {
        List<String> cloneOptionsList = new ArrayList<>(car.getOptionsList());

        Car carClone = new Car(car.getId(),
                car.getImageName(),
                car.getBrand(),
                car.getModel(),
                car.getBodyType(),
                car.getYear(),
                car.getTransmissionType(),
                car.getEngineSize(),
                car.getShortDescription(),
                car.getDescription(),
                cloneOptionsList);

        return carClone;
    }
}
