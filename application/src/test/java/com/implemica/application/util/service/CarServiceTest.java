package com.implemica.application.util.service;

import com.implemica.model.entity.Car;
import com.implemica.model.repository.CarRepository;
import com.implemica.model.service.CarServiceImpl;
import com.implemica.model.service.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.implemica.application.util.helpers.TestValues.*;
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
    public void deleteExistCarWithDefaultImage() {
        when(carRepository.existsById(1L)).thenReturn(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR));

        assertTrue(carService.deleteCarById(1L));

        verify(carRepository, times(1)).existsById(1L);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).deleteById(1L);
        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteExistCarWithNotDefaultImage() {
        when(carRepository.existsById(1L)).thenReturn(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME));

        assertTrue(carService.deleteCarById(1L));

        verify(carRepository, times(1)).existsById(1L);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).deleteById(1L);
        verify(storageService, times(1)).deleteFile(NOT_DEFAULT_IMAGE_PATH);
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
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR));
        Car foundCar = carService.findCarById(1L);

        assertEquals(EXAMPLE_CAR, foundCar);

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findCarByIdNotExistCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        Car foundCar = carService.findCarById(1L);
        assertNull(foundCar);

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void updateCarExistCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR));
        when(carRepository.save(EXAMPLE_CAR)).thenReturn(EXAMPLE_CAR);

        assertTrue(carService.updateCarById(1L, EXAMPLE_CAR_DTO));

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(EXAMPLE_CAR);
        verifyNoInteractions(storageService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void updateCarNotExist() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(carService.updateCarById(1L, EXAMPLE_CAR_DTO));

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findAllNotEmpty() {
        when(carRepository.findAll()).thenReturn(CAR_LIST);

        assertEquals(carService.findAll(), CAR_LIST);

        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void findAllEmpty() {
        when(carRepository.findAll()).thenReturn(null);

        assertNull(carService.findAll());

        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    public void uploadImageExistCarByIdWithDefaultImage() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR));
        when(carRepository.save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME))
                .thenReturn(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        when(storageService.uploadFile(MULTIPART_FILE)).thenReturn(NOT_DEFAULT_IMAGE_PATH);

        assertTrue(carService.uploadImageCarById(1L, MULTIPART_FILE));

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository,times(1)).save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        verify(storageService, times(1)).uploadFile(MULTIPART_FILE);
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(storageService);

        EXAMPLE_CAR.setImageName(DEFAULT_IMAGE_PATH);
    }

    @Test
    public void uploadImageExistCarByIdWithNotDefaultImage() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME));
        when(carRepository.save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME))
                .thenReturn(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        when(storageService.deleteFile(MULTIPART_FILE.getName())).thenReturn(true);
        when(storageService.uploadFile(MULTIPART_FILE)).thenReturn(NOT_DEFAULT_IMAGE_PATH);

        assertTrue(carService.uploadImageCarById(1L, MULTIPART_FILE));

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository,times(1)).save(EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME);
        verify(storageService, times(1)).uploadFile(MULTIPART_FILE);
        verify(storageService, times(1)).deleteFile(MULTIPART_FILE.getName());
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(storageService);
    }

    @Test
    public void uploadImageNotExistCarById() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(carService.uploadImageCarById(1L, MULTIPART_FILE));

        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(storageService);
    }
}
