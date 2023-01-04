package com.implemica.selenium.pages;

import com.implemica.selenium.helpers.CarValue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.AddCarTestValues.VALIDATION_CLASS_REG_INVALID;
import static com.implemica.selenium.helpers.BaseTestValues.*;
import static com.implemica.selenium.helpers.EditCarTestValues.XPATH_LAST_EDIT_CAR_BUTTON;

public class EditCarPage extends BaseSeleniumPage{
    //region file
    @FindBy(how = How.ID, using = "image")
    public WebElement inputImage;

    @FindBy(how = How.ID, using = "car-image")
    public WebElement image;
    //endregion

    //region brand
    @FindBy(how = How.ID, using = "brand")
    public WebElement selectBrand;
    //endregion

    //region model
    @FindBy(how = How.ID, using = "model")
    public WebElement inputModel;

    @FindBy(how = How.ID, using = "invalid-feedback-model")
    public WebElement invalidTipForInputModel;
    //endregion

    //region bodyType
    @FindBy(how = How.ID, using = "body-type")
    public WebElement selectBodyType;
    //endregion

    //region toast
    @FindBy(xpath = "//div[@id='toast-container']")
    public WebElement wrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//button[contains(@class,'toast-close-button')]")
    public WebElement closeButtonWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-title')]")
    public WebElement titleWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-message')]")
    public WebElement messageWrongToast;
    //endregion

    //region transmissionButtons
    @FindBy(how = How.ID, using = "MANUAL")
    public WebElement transmissionButtonManual;


    @FindBy(how = How.ID, using = "AUTOMATIC")
    public WebElement transmissionButtonAutomatic;
    //endregion

    //region engineSize
    @FindBy(how = How.ID, using = "engine")
    public WebElement inputEngine;

    @FindBy(how = How.ID, using = "invalid-feedback-engine")
    public WebElement invalidTipForInputEngine;
    //endregion

    //region year
    @FindBy(how = How.ID, using = "year")
    public WebElement inputYear;

    @FindBy(how = How.ID, using = "invalid-feedback-year")
    public WebElement invalidTipForInputYear;
    //endregion

    //region shortDescription
    @FindBy(how = How.ID, using = "short-description")
    public WebElement textareaShortDescription;

    @FindBy(how = How.ID, using = "invalid-feedback-short-description")
    public WebElement invalidTipForTextareaShortDescription;
    //endregion

    //region description
    @FindBy(how = How.ID, using = "description")
    public WebElement textareaDescription;


    @FindBy(how = How.ID, using = "invalid-feedback-description")
    public WebElement invalidTipForTextareaDescription;
    //endregion

    //region option field
    @FindBy(how = How.ID, using = "option")
    public WebElement inputOptionField;

    @FindBy(how = How.ID, using = "invalid-feedback-option")
    public WebElement invalidTipForInputOptionField;

    @FindBy(how = How.ID, using = "add-option-button")
    public WebElement addOptionButton;
    //endregion

    //region resetForm
    @FindBy(how = How.ID, using = "cancel-form-button")
    public WebElement cancelButton;
    //endregion

    //region modal
    @FindBy(how = How.ID, using = "cancel-modal-button")
    public WebElement cancelModalButton;

    @FindBy(how = How.ID, using = "confirm-modal-button")
    public WebElement confirmModalButton;
    //endregion

    //region header element
    @FindBy(how = How.ID, using = "add-car-button")
    public WebElement editCarButton;

    @FindBy(how = How.ID, using = "logout-button")
    public WebElement logoutButtonHeader;

    @FindBy(how = How.ID, using = "logout-modal-button")
    public WebElement logoutButtonModal;
    //endregion

    public EditCarPage(){
        PageFactory.initElements(driver, this);
    }
    public EditCarPage openEditCarPageById(String id) {
        driver.navigate().to(String.format(EDIT_URL_REG_FORMAT, id));
        return this;
    }
    public EditCarPage openEditCarPageLastCar() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        catalogAuthPage.clickByJse(catalogAuthPage.logo);

        clickByJse(driver.findElement(By.xpath(XPATH_LAST_EDIT_CAR_BUTTON)));
        return this;
    }

    public void editCar(CarValue carValue) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Select selectBrand = new Select(this.selectBrand);
        Select selectBodyType = new Select(this.selectBodyType);

        if (carValue.model != null) {
            inputModel.clear();
            inputModel.sendKeys(carValue.model);
            //enterDataByJse(inputModel, carValue.model);
        }

        if (carValue.transmissionType == MANUAL) {
            clickByJse(transmissionButtonManual);
        }

        if (carValue.transmissionType == AUTOMATIC) {
            clickByJse(transmissionButtonAutomatic);
        }

        if (carValue.engine != null) {
            inputEngine.clear();
            inputEngine.sendKeys(carValue.engine.toString());
        }

        if (carValue.year != null) {
            inputYear.clear();
            inputYear.sendKeys(carValue.year.toString());
        }

        if (carValue.bodyType != null) {
            selectBodyType.selectByVisibleText(carValue.bodyType.stringValue);
        }

        if (carValue.brand != null) {
            selectBrand.selectByVisibleText(carValue.brand.stringValue);
        }

        if (carValue.imageName != null) {
            inputImage.clear();
            inputImage.sendKeys(carValue.imageName);
        }

        if (carValue.shortDescription != null){
            textareaShortDescription.clear();
            textareaShortDescription.sendKeys(carValue.shortDescription);
            //enterDataByJse(textareaShortDescription, carValue.shortDescription);
        }

        scrollDown();

        if(carValue.description != null){
            textareaDescription.clear();
            textareaDescription.sendKeys(carValue.description);
            //enterDataByJse(textareaDescription, carValue.description);
        }

        if(carValue.options != null){
            for (int i = 0; i < carValue.options.size(); i++) {
                inputOptionField.sendKeys(carValue.options.get(i));
                //enterDataByJse(inputOptionField, carValue.options.get(i));

                if (this.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID)) {
                    break;
                }

                clickByJse(addOptionButton);
            }
        }
    }

    public LogInPage logOut(){
        webDriverWait.until(ExpectedConditions.elementToBeClickable(logoutButtonHeader));
        clickByJse(logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(logoutButtonModal));
        clickByJse(logoutButtonModal);

        return new LogInPage();
    }

    public EditCarPage clickSaveButton() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        clickByJse(editCarButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(catalogAuthPage.successToast));
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);

        return this;
    }
}
