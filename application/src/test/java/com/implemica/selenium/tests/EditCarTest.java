package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.implemica.model.enums.CarBodyType.COUPE;
import static com.implemica.model.enums.CarBrand.PORSCHE;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.AddCarTestValues.ADMIN_PASSWORD;
import static com.implemica.selenium.helpers.AddCarTestValues.ADMIN_USERNAME;
import static com.implemica.selenium.helpers.AddCarTestValues.BASE_TITLE;
import static com.implemica.selenium.helpers.AddCarTestValues.BASE_URL;
import static com.implemica.selenium.helpers.AddCarTestValues.EDIT_URL_REG_FORMAT;
import static com.implemica.selenium.helpers.AddCarTestValues.ELECTRIC;
import static com.implemica.selenium.helpers.AddCarTestValues.XPATH_FOR_IMG_ADDED_CAR;
import static com.implemica.selenium.helpers.AddCarTestValues.*;
import static com.implemica.selenium.helpers.DetailsCarTestValues.ANY_MODEL;
import static com.implemica.selenium.helpers.DetailsCarTestValues.FORMAT_OPTION_ID;
import static com.implemica.selenium.helpers.EditCarTestValues.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.*;

public class EditCarTest extends BaseSeleniumTest {
    private static DetailsCarPage detailsCarPage;
    protected static EditCarPage editCarPage;
    protected static WebDriverWait webDriverWait;

    @BeforeAll
    public static void beforeAll() {
        new LogInPage().openLoginPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD).clickAddCarButton();
        AddCarPage addCarPage = new AddCarPage();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        editCarPage = new EditCarPage();
        detailsCarPage = new DetailsCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();
    }

    @AfterAll
    public static void afterAll() {
        driver.get(BASE_URL);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_DELETE_CAR_BUTTON)));
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON)));
    }

    @ParameterizedTest
    @EnumSource(CarBrand.class)
    public void carBrand(CarBrand brand) {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.selectBrand, "value"));
        Select selectBrand = new Select(editCarPage.selectBrand);
        selectBrand.selectByVisibleText(brand.stringValue);
        editCarPage.inputModel.clear();
        editCarPage.inputModel.sendKeys(ANY_MODEL);

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.brandModelText, toTitleCase(brand.name()) + " " + ANY_MODEL));
        assertEquals(toTitleCase(brand.name()) + " " + ANY_MODEL, detailsCarPage.brandModelText.getText());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any model",
            "no model",
            "RAV4"
    })
    public void carModel(String model) {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.selectBrand, "value"));
        Select selectBrand = new Select(editCarPage.selectBrand);
        selectBrand.selectByVisibleText(PORSCHE.stringValue);
        editCarPage.inputModel.clear();
        editCarPage.inputModel.sendKeys(model);

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.brandModelText, toTitleCase(PORSCHE.name()) + " " + model));
        assertEquals(toTitleCase(PORSCHE.name()) + " " + model, detailsCarPage.brandModelText.getText());
    }

    @ParameterizedTest
    @EnumSource(CarBodyType.class)
    public void carBodyType(CarBodyType bodyType) {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.selectBodyType, "value"));
        Select selectBodyType = new Select(editCarPage.selectBodyType);
        selectBodyType.selectByValue(bodyType.name());

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.bodyTypeText, toTitleCase(bodyType.name())));
        assertEquals(toTitleCase(bodyType.name()), detailsCarPage.bodyTypeText.getText());
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.1, 10, 9.9, 4.4, 1, 5
    })
    public void carEngineValue(Double engineVolume) {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.inputEngine.clear();
        editCarPage.inputEngine.sendKeys(engineVolume.toString());

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(detailsCarPage.engineText, decimalFormat.format(engineVolume) + "L"));
        assertEquals(decimalFormat.format(engineVolume) + "L", detailsCarPage.engineText.getText());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1880, 2100, 1881, 2099
    })
    public void carYear(Integer year) {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.inputYear.clear();
        editCarPage.inputYear.sendKeys(year.toString());

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.yearText));
        assertEquals(year.toString(), detailsCarPage.yearText.getText());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any short description\n\nany short description",
            MAX_SHORT_DESCRIPTION,
            LESS_MAX_SHORT_DESCRIPTION
    })
    public void carShortDescription(String shortDescription) {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.textareaShortDescription.clear();
        editCarPage.textareaShortDescription.sendKeys(shortDescription);

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(shortDescription, detailsCarPage.shortDescriptionText.getText());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            MAX_DESCRIPTION,
            LESS_MAX_DESCRIPTION
    })
    public void carDescription(String description) {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.textareaDescription.clear();
        editCarPage.textareaDescription.sendKeys(description);

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(description, detailsCarPage.descriptionText.getText());
    }

    @ParameterizedTest
    @MethodSource("sourceValueForOptions")
    public void carOptionsAddAndDelete(List<String> options) {
        editCarPage.openEditCarPageLastCar();

        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));

        editCarPage.scrollDown();
        for (int i = 0; i < options.size(); i++) {
            editCarPage.inputOptionField.sendKeys(options.get(i));
            //enterDataByJse(inputOptionField, carValue.options.get(i));

            if (editCarPage.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID)) {
                break;
            }

            editCarPage.clickByJse(editCarPage.addOptionButton);
        }

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        options.forEach(option ->
                assertEquals(option, driver.findElement(By.id(String.format(FORMAT_OPTION_ID, option))).getText()));
        detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        options.forEach(option -> editCarPage.clickByJse(driver.findElement(By.id(String.format("delete-option-'%s'", option)))));
        editCarPage.clickSaveButton();

        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        for (int i = 0; i < options.size(); i++) {
            try {
                driver.findElement(By.id(String.format("option-'%s'", options.get(i))));
                fail("Option doesn't delete!");
            } catch (NoSuchElementException e) {
            }
        }
    }

    private static Stream<List<String>> sourceValueForOptions() {
        return Stream.of(List.of(randomAlphabetic(5), randomAlphabetic(10), randomAlphabetic(24)),
                List.of(randomAlphanumeric(14), randomAlphabetic(20)));
    }

    @Test
    public void carTransmissionAutomatic() {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.clickByJse(editCarPage.transmissionButtonAutomatic);

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        assertEquals(toTitleCase(AUTOMATIC.name()), detailsCarPage.transmissionTypeText.getText());
    }

    @Test
    public void carTransmissionManual() {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.clickByJse(editCarPage.transmissionButtonManual);

        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        assertEquals(toTitleCase(MANUAL.name()), detailsCarPage.transmissionTypeText.getText());
    }

    @Test
    public void carEngineElectric() {
        editCarPage.openEditCarPageLastCar();
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.inputEngine.clear();
        editCarPage.inputEngine.sendKeys("0");
        editCarPage.clickSaveButton();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        assertEquals(ELECTRIC, detailsCarPage.engineElectricText.getText());
    }

    @Test
    public void carImageChanged() {
        new CatalogPage().openCatalogPage();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        String imgNameBefore = detailsCarPage.image.getAttribute("src");
        detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image, "src"));
        editCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_VALID);
        editCarPage.clickSaveButton();

        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image, "src"));
        assertNotEquals(imgNameBefore, detailsCarPage.image.getAttribute("src"));
    }

    @Test
    public void carImageNotChanged() {
        new CatalogPage().openCatalogPage();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image,"src"));
        String imgNameBefore = detailsCarPage.image.getAttribute("src");
        detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(editCarPage.image,"src"));
        editCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_INVALID);
        editCarPage.wrongToast.isDisplayed();
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(editCarPage.titleWrongToast, "Invalid file"));
        assertEquals(editCarPage.titleWrongToast.getText(), "Invalid file");
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(editCarPage.messageWrongToast, "Please select" +
                " correct image format!"));
        assertEquals(editCarPage.messageWrongToast.getText(), "Please select correct image format!");
        editCarPage.clickByJse(editCarPage.editCarButton);
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(detailsCarPage.image,"src"));
        assertEquals(imgNameBefore, detailsCarPage.image.getAttribute("src"));
    }

    @Test
    public void clickCancelButtonModalConfirm() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        new AddCarPage().openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build()).clickSaveButton();
        driver.navigate().refresh();
        editCarPage.openEditCarPageLastCar();
        String carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length - 1];
        assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editCarPage.cancelButton));
        editCarPage.clickByJse(editCarPage.cancelButton);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editCarPage.confirmModalButton));
        editCarPage.clickByJse(editCarPage.confirmModalButton);

        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
        catalogAuthPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_DELETE_CAR_BUTTON)));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON)));
        catalogAuthPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON)));
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
    }

    @Test
    public void clickCancelButtonModalCancel() {
        editCarPage.openEditCarPageLastCar();
        String carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length - 1];
        assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());

        editCarPage.clickByJse(editCarPage.cancelButton);
        editCarPage.clickByJse(editCarPage.cancelModalButton);

        assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());
    }

    @Test
    public void clickEditButton() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        editCarPage.openEditCarPageLastCar();
        String carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length - 1];
        assertEquals(String.format(EDIT_URL_REG_FORMAT, carId), driver.getCurrentUrl());

        webDriverWait.until(ExpectedConditions.elementToBeClickable(editCarPage.editCarButton));
        editCarPage.clickByJse(editCarPage.editCarButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(catalogAuthPage.successToast));
        assertTrue(catalogAuthPage.successToast.isDisplayed());
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(catalogAuthPage.titleSuccessToast, TITLE_SUCCESSFULLY_CAR_EDIT));
        assertEquals(TITLE_SUCCESSFULLY_CAR_EDIT, catalogAuthPage.titleSuccessToast.getText());
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(catalogAuthPage.messageSuccessToast, MESSAGE_SUCCESSFULLY_CAR_EDIT));
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_EDIT, catalogAuthPage.messageSuccessToast.getText());
        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
    }
}
