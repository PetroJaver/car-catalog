package com.implemica.selenium.tests;

import com.implemica.model.entity.Car;
import com.implemica.model.enums.CarBodyType;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.*;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.implemica.model.enums.CarBodyType.COUPE;
import static com.implemica.model.enums.CarBrand.PORSCHE;
import static com.implemica.model.enums.CarBrand.TOYOTA;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.selenium.helpers.AddCarTestValues.INPUT_IMAGE_NAME_VALID;
import static com.implemica.selenium.helpers.BaseTestValues.*;
import static com.implemica.selenium.helpers.DetailsCarTestValues.ANY_MODEL;
import static com.implemica.selenium.helpers.LogInTestValues.MESSAGE_SUCCESSFULLY_LOGIN;
import static com.implemica.selenium.helpers.LogInTestValues.TITLE_SUCCESSFULLY_LOGIN;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogTest extends BaseSeleniumTest{

    private CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
    private CatalogPage catalogPage = new CatalogPage();
    private LogInPage logInPage = new LogInPage();

    private DetailsCarPage detailsCarPage = new DetailsCarPage();
    private AddCarPage addCarPage = new AddCarPage();
    @Test
    public void loginButton(){
        driver.navigate().refresh();
        catalogPage.openCatalogPage();
        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
        catalogPage.clickLogIn();
        assertEquals(LOGIN_URL, driver.getCurrentUrl());
        assertEquals(LOGIN_TITLE, driver.getTitle());
    }

    @Test
    public void addCarButton(){
        driver.navigate().refresh();
        catalogPage.openCatalogPage().clickLogIn();
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());

        catalogAuthPage.clickAddCarButton();
        webDriverWait.until(ExpectedConditions.urlToBe(ADD_CAR_URL));
        assertEquals(ADD_CAR_URL, driver.getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, driver.getTitle());

        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logo));
        catalogAuthPage.clickByJse(catalogAuthPage.logo);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }

    @Test
    public void logoutButton(){
        driver.navigate().refresh();
        catalogPage.clickLogIn();
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD);

        Assert.assertEquals(BASE_URL, driver.getCurrentUrl());
        Assert.assertEquals(BASE_TITLE, driver.getTitle());
        Assert.assertTrue(catalogAuthPage.addCarButton.isDisplayed());
        Assert.assertTrue(catalogAuthPage.logoutButtonHeader.isDisplayed());
        Assert.assertTrue(catalogAuthPage.userAvatar.isDisplayed());
        Assert.assertTrue(catalogAuthPage.editButtonFirstCarCard.isEnabled());
        Assert.assertTrue(catalogAuthPage.deleteButtonFirstCarCard.isEnabled());

        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);

        Assert.assertEquals(LOGIN_URL, driver.getCurrentUrl());
        Assert.assertEquals(LOGIN_TITLE, driver.getTitle());
    }

    @ParameterizedTest
    @MethodSource("sourceValueForAddCar")
    public void carCard(CarValue carValue){
        driver.navigate().refresh();
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD).clickAddCarButton();
        addCarPage.addCar(carValue).clickSaveButton();
        try{
            webDriverWait.until(ExpectedConditions.textToBe(By.xpath(String.format(XPATH_FOR_TITLE_ADDED_CAR, carValue.brand, carValue.model)), toTitleCase(carValue.brand.stringValue) + " " + carValue.model));
            assertEquals(toTitleCase(carValue.brand.stringValue)+" "+carValue.model,
                    driver.findElement(By.xpath(String.format(XPATH_FOR_TITLE_ADDED_CAR, carValue.brand,carValue.model))).getText());
            assertEquals(carValue.shortDescription,driver.findElement(By.xpath(String.format(XPATH_FOR_BODY_ADDED_CAR, carValue.brand,carValue.model))).getText());
            assertTrue(driver.findElement(By.xpath(String.format(XPATH_FOR_IMG_ADDED_CAR, carValue.brand,carValue.model))).getAttribute("src")
                    .matches(".*"+carValue.imageName.split("\\\\")[carValue.imageName.split("\\\\").length-1]));
        }finally {
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_DELETE_ADDED_CAR, carValue.brand,carValue.model))));
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_CONFIRM_DELETE_MODAL, carValue.brand,carValue.model))));
        }

        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }

    @Test
    private static Stream<CarValue> sourceValueForAddCar() {
        return Stream.of(new CarValue(TOYOTA, "Prado", CarBodyType.JEEP, AUTOMATIC, 4D,
                2006, INPUT_IMAGE_NAME_VALID, randomAlphabetic(149), randomAlphabetic(2000),
                List.of(randomAlphabetic(10), randomAlphabetic(12), randomAlphabetic(2)), true));
    }

    @Test
    public void deleteCarButton() {
        driver.navigate().refresh();
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD).clickAddCarButton();
        addCarPage.addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();

        addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
        String detailsCarURL = driver.getCurrentUrl();
        detailsCarPage.clickByJse(catalogAuthPage.logo);
        assertEquals(BASE_URL, driver.getCurrentUrl());
        webDriverWait.until(ExpectedConditions.invisibilityOf(catalogAuthPage.successToast));
        catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_DELETE_ADDED_CAR, PORSCHE.name(),ANY_MODEL))));
        catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_CONFIRM_DELETE_MODAL, PORSCHE.name(),ANY_MODEL))));
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(catalogAuthPage.titleSuccessToast, TITLE_SUCCESSFULLY_CAR_DELETE));
        assertEquals(TITLE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.titleSuccessToast.getText());
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(catalogAuthPage.messageSuccessToast, MESSAGE_SUCCESSFULLY_CAR_DELETE));
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.messageSuccessToast.getText());
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.closeButtonSuccessToast));
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
        assertEquals(BASE_URL, driver.getCurrentUrl());
        driver.navigate().to(detailsCarURL);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        assertNotEquals(detailsCarURL, driver.getCurrentUrl());

        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logo));
        catalogAuthPage.clickByJse(catalogAuthPage.logo);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }

    @Test
    public void editCarButton() {
        driver.navigate().refresh();
        String carId;
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD);
        addCarPage.clickByJse(catalogAuthPage.addCarButton);
        addCarPage.addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length - 1];
            catalogAuthPage.clickByJse(catalogAuthPage.logo);
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_EDIT_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());
            driver.navigate().back();
        } finally {
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_DELETE_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_CONFIRM_DELETE_MODAL, PORSCHE.name(), ANY_MODEL))));
        }

        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logo));
        catalogAuthPage.clickByJse(catalogAuthPage.logo);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }

    @Test
    public void deleteCarButtonThenModalCancel() {
        driver.navigate().refresh();
        String detailsUrl;
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD);
        addCarPage.clickByJse(catalogAuthPage.addCarButton);
        addCarPage.addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());

        try {
            addCarPage.clickByJse(addCarPage.addCarButton);
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            detailsUrl = driver.getCurrentUrl();
            catalogAuthPage.clickByJse(catalogAuthPage.logo);
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_DELETE_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            catalogAuthPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_CANCEL_DELETE_MODAL, PORSCHE.name(), ANY_MODEL))));
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, PORSCHE.name(), ANY_MODEL))));
            assertEquals(detailsUrl, driver.getCurrentUrl());
        } finally {
            catalogAuthPage.clickByJse(detailsCarPage.deleteCarButton);
            catalogAuthPage.clickByJse(detailsCarPage.deleteCarModalButton);
        }

        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logo));
        catalogAuthPage.clickByJse(catalogAuthPage.logo);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ADD_CAR_URL,
            LOGIN_URL
    })
    public void logo(String url){
        driver.navigate().refresh();
        logInPage.doLogin(ADMIN_USERNAME,ADMIN_PASSWORD);
        driver.get(url);
        webDriverWait.until(ExpectedConditions.urlToBe(url));
        assertEquals(url, driver.getCurrentUrl());
        catalogAuthPage.clickByJse(catalogAuthPage.logo);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        assertEquals(BASE_URL, driver.getCurrentUrl());
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonHeader));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(catalogAuthPage.logoutButtonModal));
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }
}
