package com.implemica.application.util.helpers;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.implemica.model.enums.CarBodyType.*;
import static com.implemica.model.enums.CarBrand.*;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;

public interface TestValues {
    String DESCRIPTION = "Lorem Ipsum is simply dummy text of the printing and " +
            "typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
            "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
            "It has survived not only five centuries, but also the leap into electronic typesetting, " +
            "remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset " +
            "sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like " +
            "Aldus PageMaker including versions of Lorem Ipsum.";

    String SHORT_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Etiam pharetra sagittis elit, non efficitur arcu mollis porttitor.";

    String DEFAULT_IMAGE_PATH = "defaultImageCar.png";
    String NOT_DEFAULT_IMAGE_PATH = "notDefaultImageCar.png";

    Car EXAMPLE_CAR = new Car(1L, DEFAULT_IMAGE_PATH, BMW, "Series 8", COUPE,
            2020, AUTOMATIC, 4d, SHORT_DESCRIPTION, DESCRIPTION,
            List.of("Climate control", "Signaling"));

    Car EXAMPLE_CAR_WITH_NOT_DEFAULT_IMAGE_NAME = new Car(1L, NOT_DEFAULT_IMAGE_PATH, BMW, "Series 8", COUPE,
            2020, AUTOMATIC, 4d, SHORT_DESCRIPTION, DESCRIPTION,
            List.of("Climate control", "Signaling"));

    Car EXAMPLE_CAR_WITHOUT_ID = new Car(null, DEFAULT_IMAGE_PATH, BMW, "Series 8", COUPE,
            2020, AUTOMATIC, 4d, SHORT_DESCRIPTION, DESCRIPTION,
            List.of("Climate control", "Signaling"));

    CarDTO EXAMPLE_CAR_DTO = new CarDTO(BMW.toString(), "Series 8", COUPE.toString(), 2020,
            AUTOMATIC.toString(), 4d, SHORT_DESCRIPTION, DESCRIPTION,
            List.of("Climate control", "Signaling"));

    CarDTO EXAMPLE_CAR_DTO_NOT_VALID_BRAND = new CarDTO("%not valid brand%", "Series 8",
            COUPE.toString(), 2020, AUTOMATIC.toString(), 4d, SHORT_DESCRIPTION, DESCRIPTION,
            List.of("Climate control", "Signaling"));

    CarDTO EXAMPLE_CAR_DTO_NOT_VALID_BODY_TYPE_NULL = new CarDTO("%not valid brand%", "Series 8",
            null, 2020, AUTOMATIC.toString(), 4d, SHORT_DESCRIPTION, DESCRIPTION,
            List.of("Climate control", "Signaling"));

    List<Car> CAR_LIST = List.of(
            new Car(1L, DEFAULT_IMAGE_PATH, GREAT_WALL, "RAV4", HATCHBACK,
                    2000, MANUAL, 4d, SHORT_DESCRIPTION, DESCRIPTION,
                    List.of("Climate control", "Signaling")),
            new Car(2L, NOT_DEFAULT_IMAGE_PATH, NISSAN, "Any model", SUV,
                    2010, AUTOMATIC, 4d, SHORT_DESCRIPTION, DESCRIPTION,
                    List.of("Climate control", "Signaling")),
            new Car(3L, DEFAULT_IMAGE_PATH, MAYBACH, "720S", COUPE,
                    1999, MANUAL, 4d, SHORT_DESCRIPTION, DESCRIPTION, List.of()),
            new Car(4L, NOT_DEFAULT_IMAGE_PATH, BMW, "GLA", SEDAN,
                    1890, AUTOMATIC, 4d, SHORT_DESCRIPTION, DESCRIPTION,
                    List.of("Climate control", "Signaling")),
            new Car(5L, DEFAULT_IMAGE_PATH, MCLAREN, "Series 8", MUV,
                    2020, AUTOMATIC, 4d, SHORT_DESCRIPTION, DESCRIPTION,
                    List.of("Climate control"))
    );

    MultipartFile MULTIPART_FILE = new MockMultipartFile(NOT_DEFAULT_IMAGE_PATH, HelpMethods.readFileToByteArray("src/test/resources/files/testCarImagePorshe911.png"));

}
