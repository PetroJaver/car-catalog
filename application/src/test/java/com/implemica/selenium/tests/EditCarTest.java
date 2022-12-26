package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.helpers.CreateCarForTest;
import com.implemica.selenium.pages.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

import static com.implemica.model.enums.CarBodyType.COUPE;
import static com.implemica.model.enums.CarBrand.*;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.AddCarTestValues.*;
import static com.implemica.selenium.helpers.AddCarTestValues.LESS_MAX_SHORT_DESCRIPTION;
import static com.implemica.selenium.helpers.DetailsCarTestValues.ANY_MODEL;
import static com.implemica.selenium.helpers.DetailsCarTestValues.FORMAT_OPTION_ID;
import static com.implemica.selenium.helpers.EditCarTestValues.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.*;

public class EditCarTest extends CreateCarForTest {
    private static DetailsCarPage detailsCarPage;

    @ParameterizedTest
    @EnumSource(CarBrand.class)
    public void carBrand(CarBrand brand) {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        Select selectBrand = new Select(editCarPage.selectBrand);
        selectBrand.selectByVisibleText(brand.stringValue);
        editCarPage.inputModel.clear();
        editCarPage.inputModel.sendKeys(ANY_MODEL);

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(toTitleCase(brand.name()) + " " + ANY_MODEL, detailsCarPage.brandModelText.getText());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any model",
            "no model",
            "RAV4"
    })
    public void carModel(String model) {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        Select selectBrand = new Select(editCarPage.selectBrand);
        selectBrand.selectByVisibleText(PORSCHE.stringValue);
        editCarPage.inputModel.clear();
        editCarPage.inputModel.sendKeys(model);

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(toTitleCase(PORSCHE.name()) + " " + model, detailsCarPage.brandModelText.getText());
    }

    @ParameterizedTest
    @EnumSource(CarBodyType.class)
    public void carBodyType(CarBodyType bodyType) {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        Select selectBodyType = new Select(editCarPage.selectBodyType);
        selectBodyType.selectByVisibleText(bodyType.stringValue);

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(toTitleCase(bodyType.name()), detailsCarPage.bodyTypeText.getText());
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.1, 10, 9.9, 4.4, 1, 5
    })
    public void carEngineValue(Double engineVolume) {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.inputEngine.clear();
        editCarPage.inputEngine.sendKeys(engineVolume.toString());

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(decimalFormat.format(engineVolume) + "L", detailsCarPage.engineText.getText());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1880, 2100, 1881, 2099
    })
    public void carYear(Integer year) {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.inputYear.clear();
        editCarPage.inputYear.sendKeys(year.toString());

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(year.toString(), detailsCarPage.yearText.getText());

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any short description\n\nany short description",
            MAX_SHORT_DESCRIPTION,
            LESS_MAX_SHORT_DESCRIPTION
    })
    public void carShortDescription(String shortDescription) {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.textareaShortDescription.clear();
        editCarPage.textareaShortDescription.sendKeys(shortDescription);

        editCarPage.clickByJse(editCarPage.editCarButton);
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
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.textareaDescription.clear();
        editCarPage.textareaDescription.sendKeys(description);

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.brandModelText));
        assertEquals(description, detailsCarPage.descriptionText.getText());
    }

    @ParameterizedTest
    @MethodSource("sourceValueForOptions")
    public void carOptionsAddAndDelete(List<String> options) {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();

        for (int i = 0; i < options.size(); i++) {
            editCarPage.inputOptionField.sendKeys(options.get(i));
            //enterDataByJse(inputOptionField, carValue.options.get(i));

            if (editCarPage.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID)) {
                break;
            }

            editCarPage.clickByJse(editCarPage.addOptionButton);
        }

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        options.forEach(option ->
                assertEquals(option, driver.findElement(By.id(String.format(FORMAT_OPTION_ID, option))).getText()));
        detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
        options.forEach(option -> editCarPage.clickByJse(driver.findElement(By.id(String.format("delete-option-'%s'", option)))));
        editCarPage.clickByJse(editCarPage.editCarButton);
        for (int i = 0; i < options.size(); i++) {
            try {
                driver.findElement(By.id(String.format("option-'%s'", options.get(i))));
                fail("Option doesn't delete!");
            } catch (NoSuchElementException e) {}
        }
    }

    private static Stream<List<String>> sourceValueForOptions() {
        return Stream.of(List.of(randomAlphabetic(5), randomAlphabetic(10), randomAlphabetic(24)),
                List.of(randomAlphanumeric(14), randomAlphabetic(20)));
    }

    @Test
    public void carTransmissionAutomatic() {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.clickByJse(editCarPage.transmissionButtonAutomatic);

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        assertEquals(toTitleCase(AUTOMATIC.name()), detailsCarPage.transmissionTypeText.getText());
    }

    @Test
    public void carTransmissionManual() {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.clickByJse(editCarPage.transmissionButtonManual);

        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        assertEquals(toTitleCase(MANUAL.name()), detailsCarPage.transmissionTypeText.getText());
    }

    @Test
    public void carEngineElectric() {
        detailsCarPage = new DetailsCarPage();
        editCarPage.openEditCarPageLastCar();
        editCarPage.inputEngine.clear();
        editCarPage.inputEngine.sendKeys("0");
        editCarPage.clickByJse(editCarPage.editCarButton);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));

        assertEquals(ELECTRIC, detailsCarPage.engineElectricText.getText());

    }

    @Test
    public void carImageChanged(){
        detailsCarPage = new DetailsCarPage();
        new CatalogPage().openCatalogPage();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        String imgNameBefore = detailsCarPage.image.getAttribute("src");
        detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
        editCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_VALID);
        editCarPage.clickByJse(editCarPage.editCarButton);
        assertNotEquals(imgNameBefore, detailsCarPage.image.getAttribute("src"));
    }

    @Test
    public void carImageNotChanged(){
        detailsCarPage = new DetailsCarPage();
        new CatalogPage().openCatalogPage();
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_FOR_IMG_ADDED_CAR)));
        String imgNameBefore = detailsCarPage.image.getAttribute("src");
        detailsCarPage.clickByJse(detailsCarPage.updateCarButton);
        editCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_INVALID);
        editCarPage.wrongToast.isDisplayed();
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(editCarPage.titleWrongToast,"Invalid file"));
        assertEquals(editCarPage.titleWrongToast.getText(), "Invalid file");
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(editCarPage.messageWrongToast,"Please select" +
                " correct image format!"));
        assertEquals(editCarPage.messageWrongToast.getText(), "Please select correct image format!");
        editCarPage.clickByJse(editCarPage.editCarButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(detailsCarPage.image));
        assertEquals(imgNameBefore, detailsCarPage.image.getAttribute("src"));
    }

    @Test
    public void clickCancelButtonModalConfirm() {
        new AddCarPage().openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());
        driver.navigate().refresh();
        editCarPage.openEditCarPageLastCar();
        String carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length-1];
        assertEquals(String.format(EDIT_URL_REG_FORMAT,carId), driver.getCurrentUrl());
        editCarPage.clickByJse(editCarPage.cancelButton);
        editCarPage.clickByJse(editCarPage.confirmModalButton);

        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_DELETE_CAR_BUTTON)));
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON)));
    }

    @Test
    public void clickCancelButtonModalCancel() {
        editCarPage.openEditCarPageLastCar();
        String carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length-1];
        assertEquals(String.format(EDIT_URL_REG_FORMAT,carId), driver.getCurrentUrl());

        editCarPage.clickByJse(editCarPage.cancelButton);
        editCarPage.clickByJse(editCarPage.cancelModalButton);

        assertEquals(String.format(EDIT_URL_REG_FORMAT,carId), driver.getCurrentUrl());
    }

    @Test
    public void clickEditButton(){
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        editCarPage.openEditCarPageLastCar();
        String carId = driver.getCurrentUrl().split("/")[driver.getCurrentUrl().split("/").length-1];
        assertEquals(String.format(EDIT_URL_REG_FORMAT,carId), driver.getCurrentUrl());

        editCarPage.clickByJse(editCarPage.editCarButton);
        assertTrue(catalogAuthPage.successToast.isDisplayed());
        assertEquals(TITLE_SUCCESSFULLY_CAR_EDIT,catalogAuthPage.titleSuccessToast.getText());
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_EDIT,catalogAuthPage.messageSuccessToast.getText());
        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
    }
}
