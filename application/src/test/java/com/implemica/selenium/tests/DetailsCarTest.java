package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.DetailsCarPage;
import com.implemica.selenium.pages.LogInPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
    private static CatalogAuthPage catalogAuthPage;

    @BeforeAll
    public static void beforeAll(){
        catalogAuthPage = new LogInPage().openLoginPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
        addCarPage = new AddCarPage();
        detailsCarPage = new DetailsCarPage();
    }

    @AfterAll
    public static void afterAll(){
        addCarPage.logOut();
    }

    @Test
    public void carImageNotAdded() {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.image));
            assertTrue(detailsCarPage.image.getAttribute("src").matches(".*" + DEFAULT_IMAGE_PATH));
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @Test
    public void carImageAdded() {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).imageName(INPUT_IMAGE_NAME_VALID)
                .model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.image));
            assertFalse(detailsCarPage.image.getAttribute("src").matches(".*" + DEFAULT_IMAGE_PATH));
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @Test
    public void carTransmissionAutomatic() {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL)
                .bodyType(COUPE).transmissionType(AUTOMATIC).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            detailsCarPage.scrollUp();
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.transmissionTypeText, toTitleCase(AUTOMATIC.name())));
            assertEquals(toTitleCase(AUTOMATIC.name()), detailsCarPage.transmissionTypeText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @Test
    public void carTransmissionManual() {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE)
                .transmissionType(MANUAL).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            detailsCarPage.scrollUp();
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.transmissionTypeText, toTitleCase(MANUAL.name())));
            assertEquals(toTitleCase(MANUAL.name()), detailsCarPage.transmissionTypeText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @Test
    public void carEngineElectric() {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE)
                .engine(0D).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.engineElectricText, ELECTRIC));
            assertEquals(ELECTRIC, detailsCarPage.engineElectricText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @EnumSource(CarBrand.class)
    public void carBrand(CarBrand brand) {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(brand).model(ANY_MODEL).bodyType(COUPE)
                .build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, brand.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.brandModelText
                    ,toTitleCase(brand.name()) + " " + ANY_MODEL));
            assertEquals(toTitleCase(brand.name()) + " " + ANY_MODEL, detailsCarPage.brandModelText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @EnumSource(CarBodyType.class)
    public void carBodyType(CarBodyType bodyType) {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL)
                .bodyType(bodyType).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.bodyTypeText, toTitleCase(bodyType.name())));
            assertEquals(toTitleCase(bodyType.name()), detailsCarPage.bodyTypeText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @Disabled
    @ValueSource(strings = {
            "any model",
            "no model",
            "RAV4"
    })
    public void carModel(String model) {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).bodyType(COUPE).model(model).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), model))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.brandModelText
                    , toTitleCase(PORSCHE.name()) + " " + model));
            assertEquals(toTitleCase(PORSCHE.name()) + " " + model, detailsCarPage.brandModelText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.1, 10, 9.9, 4.4, 1, 5
    })
    public void carEngineValue(Double engineVolume) {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE)
                .engine(engineVolume).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.engineText, decimalFormat.format(engineVolume) + "L"));
            assertEquals(decimalFormat.format(engineVolume) + "L", detailsCarPage.engineText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1880, 2100, 1881, 2099
    })
    public void carYear(Integer year) {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE)
                .year(year).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.yearText, year.toString()));
            assertEquals(year.toString(), detailsCarPage.yearText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any short description\n\nany short description",
            MAX_SHORT_DESCRIPTION,
            LESS_MAX_SHORT_DESCRIPTION
    })
    public void carShortDescription(String shortDescription){
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).shortDescription(shortDescription)
                .model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.shortDescriptionText, shortDescription));
            assertEquals(shortDescription, detailsCarPage.shortDescriptionText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @Disabled
    @ValueSource(strings = {
            MAX_DESCRIPTION,
            LESS_MAX_DESCRIPTION
    })
    public void carDescription(String description) {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).description(description)
                .model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.descriptionText, description));
            assertEquals(description, detailsCarPage.descriptionText.getText());
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @ParameterizedTest
    @MethodSource("sourceValueForOptions")
    public void carOptions(List<String> options) {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).options(options)
                .model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));

            options.forEach(option ->{
                    webDriverWait.until(
                    ExpectedConditions.textToBePresentInElement(driver.findElement(By.id(String.format(FORMAT_OPTION_ID, option))), option));
                    assertEquals(option, driver.findElement(By.id(String.format(FORMAT_OPTION_ID, option))).getText());});

        } finally {
            detailsCarPage.deleteCar();
        }
    }

    private static Stream<List<String>> sourceValueForOptions() {
        return Stream.of(List.of(randomAlphabetic(5), randomAlphabetic(10), randomAlphabetic(24)),
                List.of(randomAlphanumeric(14), randomAlphabetic(20)));
    }

    @Test
    public void deleteCarButton() {
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL)
                .bodyType(COUPE).build()).clickSaveButton();

        addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
        String detailsCarURL = driver.getCurrentUrl();
        detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
        detailsCarPage.clickByJse(detailsCarPage.deleteCarModalButton);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        assertEquals(TITLE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.titleSuccessToast.getText());
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.messageSuccessToast.getText());
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
        assertEquals(BASE_URL, driver.getCurrentUrl());
        driver.navigate().to(detailsCarURL);

        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        assertNotEquals(detailsCarURL, driver.getCurrentUrl());
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    public void editCarButton() {
        String carId;
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL)
                .bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length - 1];
            detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
            assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());
            driver.navigate().back();
        } finally {
            detailsCarPage.deleteCar();
        }
    }

    @Test
    public void deleteCarButtonThenModalCancel() {
        String detailsUrl;
        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL)
                .bodyType(COUPE).build()).clickSaveButton();

        try {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            detailsUrl = driver.getCurrentUrl();
            detailsCarPage.clickByJse(detailsCarPage.deleteCarButton);
            detailsCarPage.clickByJse(detailsCarPage.closeModalButton);
            assertEquals(detailsUrl, driver.getCurrentUrl());
        } finally {
            detailsCarPage.deleteCar();
        }
    }
}
