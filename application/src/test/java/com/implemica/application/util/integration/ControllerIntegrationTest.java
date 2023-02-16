package com.implemica.application.util.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.repository.CarRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static com.implemica.application.util.helpers.TestValues.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = {"/clean.sql","/fill.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    private String jwtToken;

    @Autowired
    private ObjectMapper objectMapper;

    private final String formatAuthorizationRequest = "{\"username\":\"%s\", \"password\":\"%s\"}";
    private final String formatMatchesAuthorizationReturn = "\\{\"username\":\"%s\",\"token\":\".*\"\\}";

    @Before
    public void setUp() throws Exception{
        MvcResult result = mockMvc.perform(post("/login").content(String.format(formatAuthorizationRequest, "admin", "admin"))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String responseBody = result.getResponse().getContentAsString();

        jwtToken = responseBody.split("\"")[7];
    }

    @Test
    public void authorizationTest() throws Exception {
        authorizationSuccessfulCase("admin", "admin");
        authorizationSuccessfulCase("Admin", "admin");
        authorizationSuccessfulCase("petro", "petro");
        authorizationSuccessfulCase("Petro", "petro");

        authorizationFailCase("admin", "Admin");
        authorizationFailCase("Admin", "Admin");
        authorizationFailCase("petro", "admin");
        authorizationFailCase("admin", "petro");
        authorizationFailCase("petro", "Petro");
        authorizationFailCase("Petro", "Petro");
    }

    private void authorizationSuccessfulCase(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/login").content(String.format(formatAuthorizationRequest, username, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expectedBodyReg = String.format(formatMatchesAuthorizationReturn, username.toLowerCase());

        assertTrue(responseBody.matches(expectedBodyReg));
    }

    private void authorizationFailCase(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/login").content(String.format(formatAuthorizationRequest, username, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Invalid email/password combination!\"}", responseBody);
    }

    @Test
    public void getAllCarOk() throws Exception {
        MvcResult result = mockMvc.perform(get("/cars")).andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedBody = objectMapper.writeValueAsString(CAR_LIST_H2);
        assertEquals(expectedBody, responseBody);
    }

    @Sql(value = "/clean.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void getAllCarsNoContent() throws Exception {
        mockMvc.perform(get("/cars")).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void getCarById() throws Exception {
        getCarByIdSuccessfulCase(1L, FIRST_CAR_FROM_H2);
        getCarByIdSuccessfulCase(2L, SECOND_CAR_FROM_H2);
        getCarByIdSuccessfulCase(3L, THIRD_CAR_FROM_H2);

        getCarByIdNotFoundCase(111L);
        getCarByIdNotFoundCase(201L);
    }

    public void getCarByIdSuccessfulCase(Long id, Car expectedCar) throws Exception {
        MvcResult result = mockMvc.perform(get("/cars/" + id)).andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedBody = objectMapper.writeValueAsString(expectedCar);

        assertEquals(expectedBody, responseBody);
    }

    public void getCarByIdNotFoundCase(Long id) throws Exception {
        mockMvc.perform(get("/cars/" + id)).andExpect(status().isNotFound());
    }

    @Test
    public void createCarForbidden() throws Exception{
        mockMvc.perform(post("/cars")
                .content(objectMapper.writeValueAsString(FIRST_POST_CAR_DTO_FOR_H2))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void createCarUnauthorized() throws Exception{
        MvcResult result = mockMvc.perform(post("/cars")
                .content(objectMapper.writeValueAsString(FIRST_POST_CAR_DTO_FOR_H2))
                .contentType(MediaType.APPLICATION_JSON).header("Authorization","some-token"))
                .andExpect(status().isUnauthorized()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Invalid email/password combination!\"}", responseBody);
    }

    @Test
    public void updateCarUnauthorized() throws Exception{
        MvcResult result = mockMvc.perform(put("/cars/1")
                        .content(objectMapper.writeValueAsString(FIRST_POST_CAR_DTO_FOR_H2))
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization","some-token"))
                .andExpect(status().isUnauthorized()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Invalid email/password combination!\"}", responseBody);
    }

    @Test
    public void deleteCarUnauthorized() throws Exception{
        MvcResult result = mockMvc.perform(delete("/cars/1")
                        .content(objectMapper.writeValueAsString(FIRST_POST_CAR_DTO_FOR_H2))
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization","some-token"))
                .andExpect(status().isUnauthorized()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Invalid email/password combination!\"}", responseBody);
    }

    @Test
    public void updateImageCarUnauthorized() throws Exception{
        MockMultipartFile image = new MockMultipartFile("image", NOT_DEFAULT_IMAGE_PATH, MediaType.APPLICATION_PDF_VALUE, MULTIPART_FILE.getBytes());

        MvcResult result = mockMvc.perform(multipart("/cars/1/uploadImage").file(image).contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization","some-token"))
                .andExpect(status().isUnauthorized()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Invalid email/password combination!\"}", responseBody);
    }

    @Test
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createCar() throws Exception {
        createCarSuccessful(FIRST_POST_CAR_DTO_FOR_H2, FIRST_POST_CAR_FOR_H2);

        createCarBadRequest(new CarDTO("InvalidBrand","model","SPORTCAR",2020,"AUTOMATIC",5D,SHORT_DESCRIPTION,DESCRIPTION,null));
    }

    private void createCarSuccessful(CarDTO carDTO, Car expectedCarWithoutId) throws Exception{
        MvcResult result = mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(carDTO)).header("Authorization",jwtToken)).andExpect(status().isCreated()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long id = Long.parseLong(responseBody.split("\"")[2].replaceAll("\\D", ""));
        expectedCarWithoutId.setId(id);
        String stringExpectedCar = objectMapper.writeValueAsString(expectedCarWithoutId);
        Car carInTable = carRepository.getById(id);
        String stringCarInTable = objectMapper.writeValueAsString(carInTable);
        assertEquals(stringExpectedCar, responseBody);
        assertEquals(stringExpectedCar, stringCarInTable);
    }

    private void createCarBadRequest(CarDTO carDTO) throws Exception {
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(carDTO))
                .header("Authorization",jwtToken)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateCar() throws Exception {
        updateCarSuccessful(FIRST_POST_CAR_DTO_FOR_H2, FIRST_POST_CAR_FOR_H2,1L);

        updateCarBadRequest(new CarDTO("InvalidBrand","model","SPORTCAR",2020,"AUTOMATIC",5D,SHORT_DESCRIPTION,DESCRIPTION,null),1L);

        updateCarNotFound(FIRST_POST_CAR_DTO_FOR_H2, 1111L);
    }

    private void updateCarSuccessful(CarDTO carDTO, Car expectedCarWithoutId, Long id) throws Exception{
        MvcResult result = mockMvc.perform(put("/cars/"+id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(carDTO)).header("Authorization",jwtToken)).andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        expectedCarWithoutId.setId(id);
        String stringExpectedCar = objectMapper.writeValueAsString(expectedCarWithoutId);
        Car carInTable = carRepository.getById(id);
        String stringCarInTable = objectMapper.writeValueAsString(carInTable);
        assertEquals(stringExpectedCar, responseBody);
        assertEquals(stringExpectedCar, stringCarInTable);
    }

    private void updateCarBadRequest(CarDTO carDTO,Long id) throws Exception {
        mockMvc.perform(put("/cars/"+id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(carDTO))
                .header("Authorization",jwtToken)).andExpect(status().isBadRequest());
    }

    private void updateCarNotFound(CarDTO carDTO,Long id) throws Exception {
        mockMvc.perform(put("/cars/"+id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(carDTO))
                .header("Authorization",jwtToken)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteCar() throws Exception{
        deleteCarSuccessful(1L);

        deleteCarNotFound(1111L);
    }

    private void deleteCarSuccessful(Long id) throws Exception{
        mockMvc.perform(delete("/cars/"+id).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",jwtToken)).andExpect(status().isOk());
    }

    private void deleteCarNotFound(Long id) throws Exception{
        mockMvc.perform(delete("/cars/"+id).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",jwtToken)).andExpect(status().isNotFound());
    }
}
