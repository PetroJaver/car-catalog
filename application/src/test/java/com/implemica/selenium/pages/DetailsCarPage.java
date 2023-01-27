package com.implemica.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.implemica.selenium.helpers.BaseTestValues.*;

public class DetailsCarPage extends BaseSeleniumPage {
    @FindBy(id = "logo")
    private WebElement logo;

    @FindBy(id = "brand-model")
    private WebElement brandModelText;

    @FindBy(id = "image")
    private WebElement image;

    @FindBy(id = "body-type")
    private WebElement bodyTypeText;

    @FindBy(id = "transmission")
    private WebElement transmissionTypeText;

    @FindBy(id = "engine")
    private WebElement engineText;

    @FindBy(id = "year")
    private WebElement yearText;

    @FindBy(id = "short-description")
    private WebElement shortDescriptionText;

    @FindBy(id = "description")
    private WebElement descriptionText;

    @FindBy(id = "delete-car-button")
    private WebElement deleteCarButton;

    @FindBy(id = "update-car-button")
    private WebElement editCarButton;

    @FindBy(id = "confirm-car-modal-button")
    private WebElement confirmDeleteCarModalButton;

    @FindBy(id = "cancel-modal-button")
    private WebElement cancelDeleteCarModalButton;

    @FindBy(id = "delete-car-modal")
    private WebElement deleteCarModal;


    public DetailsCarPage() {
        PageFactory.initElements(driver, this);
    }

    public String getTitleCar(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(brandModelText, waitedValue));

        return brandModelText.getText();
    }

    public String getEngineCar(String waitedValue) {
        String engine = null;

        if (waitedValue != null) {
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(engineText, waitedValue));
            engine = engineText.getText();
        }

        return engine;
    }

    public String getBodyTypeCar(String waitedValue) {
        String bodyType = null;

        if (waitedValue != null) {
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(bodyTypeText, waitedValue));
            bodyType = bodyTypeText.getText();
        }

        return bodyType;
    }

    public String getTransmissionTypeCar(String waitedValue) {
        String transmissionType = null;
        if (waitedValue != null) {
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(transmissionTypeText, waitedValue));
            transmissionType = transmissionTypeText.getText();
        }
        return transmissionType;
    }

    public String getImageSrc() {
        webDriverWait.until(ExpectedConditions.visibilityOf(image));

        return image.getAttribute("src");
    }

    public String getShortDescriptionCar(String waitedValue) {
        String shortDescription = null;

        if (waitedValue != null) {
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(shortDescriptionText, waitedValue));
            shortDescription = shortDescriptionText.getText();
        }

        return shortDescription;
    }

    public String getDescriptionCar(String waitedValue) {
        String description = null;

        if (waitedValue != null) {
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(descriptionText, waitedValue));
            description = descriptionText.getText();
        }

        return description;
    }

    public List<String> getAllOptions() {
        List<String> listOptions = driver.findElements(By.className("option-item"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        return listOptions.isEmpty() ? null : listOptions;
    }

    public DetailsCarPage clickDeleteButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(deleteCarButton));
        clickByJse(deleteCarButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(deleteCarModal));

        return this;
    }

    public EditCarPage clickEditButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(editCarButton));
        clickByJse(editCarButton);
        webDriverWait.until(ExpectedConditions.urlMatches(EDIT_URL_MATCHES));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_PART_EDIT));

        return new EditCarPage();
    }

    public CatalogAuthPage clickConfirmDeleteCarModal() {
        webDriverWait.until(ExpectedConditions.visibilityOf(confirmDeleteCarModalButton));
        clickByJse(confirmDeleteCarModalButton);
        //webDriverWait.until(ExpectedConditions.invisibilityOf(deleteCarModal));
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));

        return new CatalogAuthPage();
    }

    public CatalogAuthPage clickLogo() {
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(logo));
        clickByJse(logo);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_BASE_PAGE));

        return new CatalogAuthPage();
    }

    public DetailsCarPage clickCancelDeleteCarModal() {
        webDriverWait.until(ExpectedConditions.visibilityOf(cancelDeleteCarModalButton));
        clickByJse(cancelDeleteCarModalButton);
        webDriverWait.until(ExpectedConditions.invisibilityOf(deleteCarModal));
        webDriverWait.until(ExpectedConditions.urlMatches(DETAILS_URL_MATCHES));

        return this;
    }
}
