package com.implemica.application.util.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.model.service.CarServiceImpl;
import com.implemica.security.JwtAuthenticationException;
import com.implemica.security.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarServiceImpl carService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

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

    public ControllerTest() {
    }

    @Test
    public void getAllTestCarsFoundShouldReturnFoundCars() throws Exception {
        List<Car> carList = Arrays.asList(secondCar, firstCar,secondCar);
        when(carService.findAll()).thenReturn(carList);

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(carList)));

        verify(carService, times(1)).findAll();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getAllTestNoCarsShouldNotReturnAnything() throws Exception {
        when(carService.findAll()).thenReturn(null);

        mockMvc.perform(get("/cars")).andExpect(status().isNoContent());

        verify(carService, times(1)).findAll();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getByIdCarNotFoundShouldReturnHttpStatusCode404() throws Exception {
        when(carService.findCarById(1L)).thenReturn(null);

        mockMvc.perform(get("/cars/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(carService, times(1)).findCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getByIdCarFoundShouldReturnFoundCar() throws Exception {
        when(carService.findCarById(1L)).thenReturn(firstCar);

        mockMvc.perform(get("/cars/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstCar)));

        verify(carService, times(1)).findCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarShouldReturnHttpStatus201() throws Exception {
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");

        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile carDtoPart = new MockMultipartFile("carDto", "",
                "application/json", objectMapper.writeValueAsBytes(firstCarDto));

        mockMvc.perform(multipart("/cars")
                        .file(file)
                        .file(carDtoPart)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isCreated());

        verify(carService, times(1)).saveCar(any(CarDTO.class), any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarMethodArgumentNotValidExceptionException() throws Exception{
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");

        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile carDtoPart = new MockMultipartFile("carDto", "",
                "application/json", objectMapper.writeValueAsBytes(noValidBrandCarDto));

        mockMvc.perform(multipart("/cars")
                        .file(file)
                        .file(carDtoPart)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"brand\": \"Invalid brand\"}"));

        verify(carService, times(0)).saveCar(any(CarDTO.class), any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarBindExceptionAllArgumentDtoNull() throws Exception{
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");

        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile carDtoPart = new MockMultipartFile("carDto", "",
                "application/json", objectMapper.writeValueAsBytes(new CarDTO()));

        mockMvc.perform(multipart("/cars")
                        .file(file)
                        .file(carDtoPart)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        verify(carService, times(0)).saveCar(any(CarDTO.class), any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void updateCarSuccessfulShouldReturnHttpStatus200() throws Exception {
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");

        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile carDtoPart = new MockMultipartFile("carDto", "",
                "application/json", objectMapper.writeValueAsBytes(firstCarDto));

        when(carService.update(eq(1L),any(CarDTO.class),any(MultipartFile.class))).thenReturn(true);

        mockMvc.perform(multipart("/cars/1")
                        .file(file)
                        .file(carDtoPart)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(carService, times(1)).update(eq(1L),any(CarDTO.class),any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void updateCarNotSuccessfulShouldReturnHttpStatus400() throws Exception {
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");

        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile carDtoPart = new MockMultipartFile("carDto", "",
                "application/json", objectMapper.writeValueAsBytes(firstCarDto));

        when(carService.update(eq(1L),any(CarDTO.class),any(MultipartFile.class))).thenReturn(false);

        mockMvc.perform(multipart("/cars/1")
                        .file(file)
                        .file(carDtoPart)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());

        verify(carService, times(1)).update(eq(1L),any(CarDTO.class),any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void updateCarInvalidMissingServletRequestPartExceptionForCarDto() throws Exception{
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");

        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        when(carService.update(eq(1L),any(CarDTO.class),any(MultipartFile.class))).thenReturn(true);

        mockMvc.perform(multipart("/cars/1")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Required request part 'carDto' is not present\"}"));

        verify(carService, times(0)).update(eq(1L),any(CarDTO.class),any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void deleteCarSuccessfulShouldReturnHttpStatus200() throws Exception{
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");
        when(carService.deleteCarById(1L)).thenReturn(true);

        mockMvc.perform(delete("/cars/1").header("Authorization", token))
                .andExpect(status().isOk());

        verify(carService,times(1)).deleteCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void deleteCarNotSuccessfulShouldReturnHttpStatus400() throws Exception{
        String token = jwtTokenProvider.createToken("admin@admin.com", "ADMIN");
        when(carService.deleteCarById(1L)).thenReturn(false);

        mockMvc.perform(delete("/cars/1").header("Authorization", token))
                .andExpect(status().isBadRequest());

        verify(carService,times(1)).deleteCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarWithoutAuthorization() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", "testContract.pdf",
                MediaType.APPLICATION_PDF_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile carDtoPart = new MockMultipartFile("carDto", "",
                "application/json", objectMapper.writeValueAsBytes(firstCarDto));

        boolean testIsFail = true;
        try{
            mockMvc.perform(multipart("/cars")
                            .file(file)
                            .file(carDtoPart)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "noValidToken"));
        }catch (JwtAuthenticationException e){
            testIsFail = false;
        }

        assertFalse(testIsFail);

        verifyNoInteractions(carService);
    }
}
