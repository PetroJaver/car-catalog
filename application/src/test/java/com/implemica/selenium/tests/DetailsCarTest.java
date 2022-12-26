package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.DetailsCarPage;
import com.implemica.selenium.pages.LogInPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.implemica.application.util.helpers.TestValues.DEFAULT_IMAGE_PATH;
import static com.implemica.model.enums.CarBodyType.COUPE;
import static com.implemica.model.enums.CarBrand.PORSCHE;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.AddCarTestValues.ADMIN_PASSWORD;
import static com.implemica.selenium.helpers.AddCarTestValues.ADMIN_USERNAME;
import static com.implemica.selenium.helpers.AddCarTestValues.BASE_URL;
import static com.implemica.selenium.helpers.AddCarTestValues.EDIT_URL_REG_FORMAT;
import static com.implemica.selenium.helpers.AddCarTestValues.ELECTRIC;
import static com.implemica.selenium.helpers.AddCarTestValues.MESSAGE_SUCCESSFULLY_CAR_DELETE;
import static com.implemica.selenium.helpers.AddCarTestValues.TITLE_SUCCESSFULLY_CAR_DELETE;
import static com.implemica.selenium.helpers.AddCarTestValues.*;
import static com.implemica.selenium.helpers.DetailsCarTestValues.XPATH_FORMAT_FOR_IMG_ADDED_CAR;
import static com.implemica.selenium.helpers.DetailsCarTestValues.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.*;

public class DetailsCarTest extends BaseSeleniumTest {
    private static AddCarPage addCarPage;
    private static DetailsCarPage detailsCarPage;

    public DetailsCarTest() {
        if (addCarPage == null){
            new LogInPage().openLoginPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD).clickAddCarButton();
            addCarPage = new AddCarPage();
        }
    }

    @Test
    public void carImageNotAdded() {
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertTrue(detailsCarPage.image.getAttribute("src").matches(".*" + DEFAULT_IMAGE_PATH));
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @Test
    public void carImageAdded() {
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).imageName(INPUT_IMAGE_NAME_VALID).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertFalse(detailsCarPage.image.getAttribute("src").matches(".*" + DEFAULT_IMAGE_PATH));
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @Test
    public void carTransmissionAutomatic() {
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).transmissionType(AUTOMATIC).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(toTitleCase(AUTOMATIC.name()), detailsCarPage.transmissionTypeText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @Test
    public void carTransmissionManual() {
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).transmissionType(MANUAL).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(toTitleCase(MANUAL.name()), detailsCarPage.transmissionTypeText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @Test
    public void carEngineElectric() {
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).engine(0D).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(ELECTRIC, detailsCarPage.engineElectricText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @EnumSource(CarBrand.class)
    public void carBrand(CarBrand brand) {
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(brand).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, brand.name(), ANY_MODEL))));

            assertEquals(toTitleCase(brand.name()) + " " + ANY_MODEL, detailsCarPage.brandModelText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @EnumSource(CarBodyType.class)
    public void carBodyType(CarBodyType bodyType) {
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(bodyType).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(toTitleCase(bodyType.name()), detailsCarPage.bodyTypeText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any model",
            "no model",
            "RAV4"
    })
    public void carModel(String model) {
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).bodyType(COUPE).model(model).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), model))));

            assertEquals(toTitleCase(PORSCHE.name()) + " " + model, detailsCarPage.brandModelText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.1, 10, 9.9, 4.4, 1, 5
    })
    public void carEngineValue(Double engineVolume) {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE)
                .engine(engineVolume).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(decimalFormat.format(engineVolume) + "L", detailsCarPage.engineText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1880, 2100, 1881, 2099
    })
    public void carYear(Integer year) {
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE)
                .year(year).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(year.toString(), detailsCarPage.yearText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any short description\n\nany short description",
            MAX_SHORT_DESCRIPTION,
            LESS_MAX_SHORT_DESCRIPTION
    })
    public void carShortDescription(String shortDescription) {
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).shortDescription(shortDescription)
                .model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(shortDescription, detailsCarPage.shortDescriptionText.getText());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            MAX_DESCRIPTION,
            LESS_MAX_DESCRIPTION
    })
    public void carDescription(String description) {
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).description(description)
                .model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            assertEquals(description, detailsCarPage.descriptionText.getText());
            ;
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @ParameterizedTest
    @MethodSource("sourceValueForOptions")
    public void carOptions(List<String> options) {
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).options(options)
                .model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            options.forEach(option ->
                    assertEquals(option, driver.findElement(By.id(String.format(FORMAT_OPTION_ID, option))).getText()));

        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    private static Stream<List<String>> sourceValueForOptions() {
        return Stream.of(List.of(randomAlphabetic(5), randomAlphabetic(10), randomAlphabetic(24)),
                List.of(randomAlphanumeric(14), randomAlphabetic(20)));
    }

    @Test
    public void deleteCarButton() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        detailsCarPage = new DetailsCarPage();
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());

        addCarPage.clickByJse(addCarPage.addCarButton);
        addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
        String detailsCarURL = driver.getCurrentUrl();
        detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
        detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlToBe(BASE_URL));
        assertEquals(TITLE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.titleSuccessToast.getText());
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.messageSuccessToast.getText());
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
        assertEquals(BASE_URL, driver.getCurrentUrl());
        driver.navigate().to(detailsCarURL);
        assertNotEquals(detailsCarURL, driver.getCurrentUrl());
    }

    @Test
    public void editCarButton() {
        String carId;
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length - 1];
            detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
            assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());
            driver.navigate().back();
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }

    @Test
    public void deleteCarButtonThenModalCancel() {
        String detailsUrl;
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            detailsUrl = driver.getCurrentUrl();
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.closeModalButton);
            assertEquals(detailsUrl, driver.getCurrentUrl());
        } finally {
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }
    }
}
