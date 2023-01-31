import com.swagger.implemica.client.codegen.rest.api.AuthenticationApi;
import com.swagger.implemica.client.codegen.rest.api.CarApi;
import com.swagger.implemica.client.codegen.rest.invoker.ApiClient;
import com.swagger.implemica.client.codegen.rest.model.AuthenticationRequestDTO;
import com.swagger.implemica.client.codegen.rest.model.Car;
import com.swagger.implemica.client.codegen.rest.model.CarDTO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class CarApiTest {
    private static CarApi carApiAuthorized;
    private static final CarDTO carDTOValidOne = new CarDTO().brand(CarDTO.BrandEnum.BMW)
            .model("X5")
            .bodyType(CarDTO.BodyTypeEnum.SUV)
            .transmissionType(CarDTO.TransmissionTypeEnum.AUTOMATIC)
            .engineSize(3D)
            .year(2018)
            .shortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
            .description("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.")
            .optionsList(List.of("ABS", "Climate control", "Heated windshield", "Signaling"));

    private static final CarDTO carDTOValidTwo = new CarDTO().brand(CarDTO.BrandEnum.PORSCHE)
            .model("Panamera")
            .bodyType(CarDTO.BodyTypeEnum.SPORTCAR)
            .transmissionType(CarDTO.TransmissionTypeEnum.AUTOMATIC)
            .engineSize(3D)
            .year(2018)
            .shortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
            .description("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.")
            .optionsList(List.of("ABS"));

    private static final CarDTO carDTOInvalidYear = new CarDTO().brand(CarDTO.BrandEnum.BMW)
            .model("X5")
            .bodyType(CarDTO.BodyTypeEnum.SUV)
            .transmissionType(CarDTO.TransmissionTypeEnum.AUTOMATIC)
            .engineSize(3D)
            .year(1879)
            .shortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
            .description("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.")
            .optionsList(List.of("ABS", "Climate control", "Heated windshield", "Signaling"));

    private static final AuthenticationApi auth = new AuthenticationApi();

    @BeforeClass
    public static void setUp() {
        String token = auth.authenticateUsingPOST(new AuthenticationRequestDTO().username("admin").password("admin")).getToken();
        carApiAuthorized = new CarApi(new ApiClient().addDefaultHeader("Authorization", token));
    }

    @Test
    public void getAllCarsTest() {
        ResponseEntity<List<Car>> response = carApiAuthorized.getAllUsingGETWithHttpInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void createCarAndGetByIdAndEditAndDeleteTest() {
        ResponseEntity<Car> response = carApiAuthorized.createUsingPOSTWithHttpInfo(carDTOValidOne);
        Car responseCar = response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = carApiAuthorized.getUsingGETWithHttpInfo(responseCar.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        responseCar = response.getBody();
        Long responseCarId = responseCar.getId();

        try {
            assertEquals(carDTOValidOne.getBrand().getValue(), responseCar.getBrand().getValue());
            assertEquals(carDTOValidOne.getModel(), responseCar.getModel());
            assertEquals(carDTOValidOne.getBodyType().getValue(), responseCar.getBodyType().getValue());
            assertEquals(carDTOValidOne.getTransmissionType().getValue(), responseCar.getTransmissionType().getValue());
            assertEquals(carDTOValidOne.getEngineSize(), responseCar.getEngineSize());
            assertEquals(carDTOValidOne.getYear(), responseCar.getYear());
            assertEquals(carDTOValidOne.getShortDescription(), responseCar.getShortDescription());
            assertEquals(carDTOValidOne.getDescription(), responseCar.getDescription());
            assertEquals(carDTOValidOne.getOptionsList(), responseCar.getOptionsList());
        } catch (Exception e){
            carApiAuthorized.deleteUsingDELETE(responseCarId);
            assertThrows(HttpClientErrorException.NotFound.class, () -> carApiAuthorized.getUsingGET(responseCarId));
        }

        response = carApiAuthorized.updateUsingPUTWithHttpInfo(carDTOValidTwo, responseCarId);
        responseCar = response.getBody();

        try {
            assertEquals(carDTOValidTwo.getBrand().getValue(), responseCar.getBrand().getValue());
            assertEquals(carDTOValidTwo.getModel(), responseCar.getModel());
            assertEquals(carDTOValidTwo.getBodyType().getValue(), responseCar.getBodyType().getValue());
            assertEquals(carDTOValidTwo.getTransmissionType().getValue(), responseCar.getTransmissionType().getValue());
            assertEquals(carDTOValidTwo.getEngineSize(), responseCar.getEngineSize());
            assertEquals(carDTOValidTwo.getYear(), responseCar.getYear());
            assertEquals(carDTOValidTwo.getShortDescription(), responseCar.getShortDescription());
            assertEquals(carDTOValidTwo.getDescription(), responseCar.getDescription());
            assertEquals(carDTOValidTwo.getOptionsList(), responseCar.getOptionsList());
        } finally {
            carApiAuthorized.deleteUsingDELETE(responseCarId);
            assertThrows(HttpClientErrorException.NotFound.class, () -> carApiAuthorized.getUsingGET(responseCarId));
        }
    }

    @Test
    public void getByIdReturnNotFound() {
        List<Long> existCarIdList = carApiAuthorized.getAllUsingGET().stream().map(Car::getId).collect(Collectors.toList());
        Long notExistCarId = Stream.generate(this::getRandomId).filter(id -> !existCarIdList.contains(id)).findFirst().orElseThrow();

        assertThrows(HttpClientErrorException.NotFound.class, () -> carApiAuthorized.getUsingGET(notExistCarId));
    }

    @Test
    public void createCarWhenReturnBadRequest() {
        assertThrows(HttpClientErrorException.BadRequest.class,
                () -> carApiAuthorized.createUsingPOSTWithHttpInfo(carDTOInvalidYear));
    }

    public Long getRandomId() {
        return (long) (Long.MAX_VALUE * Math.random());
    }
}
