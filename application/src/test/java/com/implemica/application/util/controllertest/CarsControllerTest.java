package com.implemica.application.util.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.implemica.model.exceptions.CarNotFoundException;
import com.implemica.model.service.CarService;
import com.implemica.security.JwtAuthenticationException;
import com.implemica.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.implemica.application.util.helpers.TestValues.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private String jwtToken;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void before() {
        jwtToken = jwtTokenProvider.createToken("admin", "ADMIN");
        cacheManager.getCache("cars").clear();
        cacheManager.getCache("carsList").clear();
    }

    @Test
    public void getAllCarsStatusOk() throws Exception {
        when(carService.findAll()).thenReturn(CAR_LIST);

        mockMvc.perform(get("/cars")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(CAR_LIST)));

        verify(carService, times(1)).findAll();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getAllCarsStatusNoContent() throws Exception {
        when(carService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/cars")).andExpect(status().isNoContent());

        verify(carService, times(1)).findAll();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getCarByIdStatusNotFound() throws Exception {
        when(carService.findCarById(1L)).thenReturn(null);

        mockMvc.perform(get("/cars/{id}", 1L)).andExpect(status().isNotFound());

        verify(carService, times(1)).findCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getCarByIdStatusOk() throws Exception {
        when(carService.findCarById(1L)).thenReturn(EXAMPLE_CAR);

        mockMvc.perform(get("/cars/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(EXAMPLE_CAR)));

        verify(carService, times(1)).findCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarStatusCreated() throws Exception {
        when(carService.saveCar(eq(EXAMPLE_CAR_DTO))).thenReturn(EXAMPLE_CAR);

        mockMvc.perform(post("/cars").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO))
                        .contentType(APPLICATION_JSON).header("Authorization", jwtToken))
                .andExpect(status().isCreated());

        verify(carService, times(1)).saveCar(eq(EXAMPLE_CAR_DTO));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarStatusConflict() throws Exception {
        when(carService.saveCar(eq(EXAMPLE_CAR_DTO))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/cars").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO))
                        .contentType(APPLICATION_JSON).header("Authorization", jwtToken))
                .andExpect(status().isConflict());

        verify(carService, times(1)).saveCar(eq(EXAMPLE_CAR_DTO));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarMethodArgumentNotValidBrand() throws Exception {
        mockMvc.perform(post("/cars")
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO_NOT_VALID_BODY_TYPE_NULL))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"brand\": \"Invalid brand\"}"));

        verifyNoInteractions(carService);
    }

    @Test
    public void createCarMethodArgumentNotValidBrandNull() throws Exception {
        mockMvc.perform(post("/cars")
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO_NOT_VALID_BRAND))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"brand\": \"Invalid brand\"}"));

        verifyNoInteractions(carService);
    }

    @Test
    public void updateCarStatusOk() throws Exception {
        when(carService.updateCarById(eq(1L), eq(EXAMPLE_CAR_DTO))).thenReturn(EXAMPLE_CAR);

        mockMvc.perform(put("/cars/1")
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk());

        verify(carService, times(1)).updateCarById(eq(1L), eq(EXAMPLE_CAR_DTO));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void updateCarStatusNotFound() throws Exception {
        when(carService.updateCarById(eq(1L), eq(EXAMPLE_CAR_DTO))).thenThrow(new CarNotFoundException("File was not deleted from AWS S3 storage, something is wrong with AmazonS3."));

        mockMvc.perform(put("/cars/1")
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isNotFound());

        verify(carService, times(1)).updateCarById(eq(1L), eq(EXAMPLE_CAR_DTO));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void updateCarStatusConflict() throws Exception {
        when(carService.updateCarById(eq(1L), eq(EXAMPLE_CAR_DTO))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(put("/cars/1")
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isConflict());

        verify(carService, times(1)).updateCarById(eq(1L), eq(EXAMPLE_CAR_DTO));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void deleteCarSuccessfulShouldReturnHttpStatus200() throws Exception {
        mockMvc.perform(delete("/cars/1").header("Authorization", jwtToken))
                .andExpect(status().isOk());

        verify(carService, times(1)).deleteCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void deleteCarBadRequest() throws Exception {
        doThrow(new CarNotFoundException("File was not deleted from AWS S3 storage, something is wrong with AmazonS3.")).when(carService).deleteCarById(1L);

        mockMvc.perform(delete("/cars/1").header("Authorization", jwtToken))
                .andExpect(status().isNotFound());

        verify(carService, times(1)).deleteCarById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createCarWithoutAuthorization() throws Exception {
        mockMvc.perform(post("/cars/")
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(EXAMPLE_CAR_DTO))
                .contentType(APPLICATION_JSON)
                .header("Authorization", "noValidToken"))
                .andExpect(content().json("{\"message\":\"Invalid email/password combination!\"}"));

        verifyNoInteractions(carService);
    }

    @Test
    public void uploadImageCarByIdStatusOk() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", NOT_DEFAULT_IMAGE_PATH,
                MediaType.APPLICATION_PDF_VALUE, MULTIPART_FILE.getBytes());

        mockMvc.perform(multipart("/cars/1/uploadImage/")
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk());

        verify(carService, times(1)).uploadImageCarById(eq(1L), any(MultipartFile.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void uploadImageCarByIdStatusBadRequest() throws Exception {
        mockMvc.perform(multipart("/cars/1/uploadImage/")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(carService);
    }
}
